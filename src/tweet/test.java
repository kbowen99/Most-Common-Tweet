package tweet;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class test {
	
	ArrayList<Status> statuses = new ArrayList<>();
	ArrayList<String> stringStatus = new ArrayList<>();
	ArrayList<String> commonWords = new ArrayList<>();
	ArrayList<String> logFile = new ArrayList<>();
	Twitter twitter = new TwitterFactory().getInstance();
	String user;
	boolean limited;
	boolean youtube;
	
	public test(String user, boolean limitCount, boolean youtubeify){
		this.youtube = youtubeify;
		this.limited = limitCount;
		this.user = user;
		loadFile();
		loadTweets();
		p("Spamming Now");
		spamConsole();
		p("Spam Complete");
		//sendTweet(user + " First Tweet:" + statuses.get(statuses.size() - 1).getText());
		p("Scrubbing Tweets");
		scrubTweets();
		p("Spamming Cleaner Tweets");
		spamConsole();
		p("Calculating Common Words");
		//getCommonWords();
		getCommonKWords();
		p("Dumping Log File");
		dumpLogFile();
	}
	
	/**
	 * Writes all statuses in the arraylist 'statuses' to the console
	 */
	public void spamConsole(){
		for (String s : stringStatus)
			p(s);
	}
	
	/**
	 * Posts a Tweet on your twitter
	 * Automagically shortens tweet to 140 char
	 * @param s String to Tweet
	 */
	public void sendTweet(String s){
		try {
			twitter.updateStatus(((s.length() > 140) ? s.substring(0,140) : s));
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads all tweets for a user (uses the user variable)
	 */
	private void loadTweets(){
		p("Magic Finding");
		//Loads tweets using pagination
		int pageno = 1; 
		boolean complete = false;
		while (!complete) {
		  try {
		    int size = statuses.size(); 
		    Paging page = new Paging(pageno++, 100);
		    statuses.addAll(twitter.getUserTimeline(user, page));
		    if (statuses.size() == size || (limited ? (statuses.size() >= 2000) : false))
		    	complete = true;
		  }
		  catch(TwitterException e) {
			  //Hopefully not used
		    e.printStackTrace();
		  }
		}
		for (Status s : statuses){
			//Converts and Adds statuses into string statuses
			stringStatus.add( " " + s.getText());
		}
		p(statuses.size() + " Magics Found");
	}
	
	/**
	 * Loads "commonWords.txt" from resources into the arraylist commonWords
	 */
	private void loadFile(){
		try (BufferedReader br = new BufferedReader(new FileReader("Resources\\commonWords.txt"))){
			String line = br.readLine();
			while(line != null) {
				commonWords.add(line);
				line = br.readLine();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Scrubs all tweets in arraylist 'statuses' from words in the arraylist 'commonwords'
	 * *Assumes file has been loaded
	 */
	private void scrubTweets(){
		for (int s = 0; s < stringStatus.size(); s++){
			youtubeRun(stringStatus.get(s));
			for (int w = 0; w < commonWords.size(); w++){
				stringStatus.set(s, stringStatus.get(s).replace("@", ""));
				//stringStatus.set(s, stringStatus.get(s).replace("rt", ""));
				 stringStatus.set(s, stringStatus.get(s).toLowerCase().replace((" " + commonWords.get(w).toLowerCase() + " "), " "));
				 if (w == 0)
					 stringStatus.set(s, stringStatus.get(s).toLowerCase().replace((commonWords.get(w).toLowerCase() + " "), " "));
			}
		}
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	/**
	 * Gets Most Common word using a hashmap
	 */
	private void getCommonWords(){
		HashMap<String, Integer> hmap = new HashMap<>();
		for (String stat : stringStatus) {
			String[] magic = stat.split(" ");
			for (String word : magic){
				String clean = word.replaceAll("[^\\p{Alnum}]+", "");
				//Magical HashMaps
				Integer c = hmap.get(clean);
				if(c == null) c = new Integer(0);
				c++;
				hmap.put(clean,c);
			}
		}
		Map.Entry<String,Integer> mostRepeated = null;
		for(Map.Entry<String, Integer> e: hmap.entrySet())
		{
		    if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
		        mostRepeated = e;
		}
		p("Most Repeated word: " + mostRepeated.getKey() + ", " + mostRepeated.getValue().intValue() + " Times");
	}
	
	/**
	 * Gets Most Common Word using Kword variables
	 * HashMaps are hard...
	 */
	private void getCommonKWords(){
		ArrayList<KWord> words = new ArrayList<>();
		
		for (String stat : stringStatus) {
			String[] magic = stat.split(" ");
			for (String word : magic){
				String clean = word.replaceAll("[^\\p{Alnum}]+", "");
				try{
					words.get(findWord(words, clean.trim().toLowerCase())).increment();
				} catch(Exception e) {}
			}
		}
		Collections.sort(words); //Sorts the Code AUTOMAGICALLY
		KWord maxCount = new KWord("NO WORDS");
		for(KWord k : words){
			p(k.toString());
			if(k.getCount() > maxCount.getCount())
				maxCount = k;
		}
		JOptionPane.showMessageDialog(null, p(this.user + " said '" + maxCount.getWord() + "' " + maxCount.getCount() + " times in " + statuses.size() + " Tweets (" + (int)(((double)maxCount.getCount()/(double)statuses.size()) * 100)) + "%)".toString());
	}
	
	/**
	 * finds the given word's index in the arraylist, adds the word if not found
	 * @param list List to Search through
	 * @param Word Word to find or add
	 * @return the index of the word
	 */
	private int findWord(ArrayList<KWord> list, String Word){
		if (Word.trim() == "" || Word.length() < 1)
			return -1;
		for(int k = 0; k < list.size(); k++){
			if (list.get(k).getWord().equals(Word))
				return k;
		}
		list.add(new KWord(Word, 0));
		return findWord(list, Word);
	}
	
	
	/**
	 * Checks and Opens if Youtube
	 */
	private void youtubeRun(String maybeLink){
		if (youtube){
			String tmpLink = "";
			if (maybeLink.contains("https://youtu")){
				tmpLink = maybeLink.substring(maybeLink.indexOf("https://youtu"));
				tmpLink = tmpLink.substring(0, tmpLink.indexOf(" "));
			} else if (maybeLink.contains("https://www.youtu")){
				tmpLink = maybeLink.substring(maybeLink.indexOf("https://www.youtu"));
				tmpLink = tmpLink.substring(0, tmpLink.indexOf(" "));
			}
			if (tmpLink != ""){
				int confirm = JOptionPane.showConfirmDialog(null, ("Open '" + tmpLink + "' ?"));
				if (confirm == JOptionPane.OK_OPTION){
					try {
						Desktop.getDesktop().browse(new URI("http://www.example.com"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (confirm == JOptionPane.CANCEL_OPTION)
					youtube = false;
			}
		}
	}
	
	/**
	 * Prints out to console (is much easier to type)
	 * @param P Object to print
	 */
	private Object p(Object P){
		System.out.println(P); logFile.add(P + ""); return P;
	}
	
	/**
	 * called at the end of code to dump the logFile array into a file
	 */
	private void dumpLogFile(){
		try {
			FileWriter writ;
			writ = new FileWriter("log.txt");
			for (String w : logFile)
				writ.write(w + "\r\n");
			writ.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
