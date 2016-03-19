package tweet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class test {
	
	ArrayList<Status> statuses = new ArrayList<>();
	ArrayList<String> stringStatus = new ArrayList<>();
	ArrayList<String> commonWords = new ArrayList<>();
	HashMap<String, Integer> hmap = new HashMap<>();
	Twitter twitter = new TwitterFactory().getInstance();
	String user;
	
	public test(String user){
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
		getCommonWords();
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
		int pageno = 1;
		boolean complete = false;
		while (!complete) {
		  try {
		    int size = statuses.size(); 
		    Paging page = new Paging(pageno++, 100);
		    statuses.addAll(twitter.getUserTimeline(user, page));
		    if (statuses.size() == size || (statuses.size() >= 2000))
		    	complete = true;
		  }
		  catch(TwitterException e) {
		    e.printStackTrace();
		  }
		}
		for (Status s : statuses){
			stringStatus.add(s.getText());
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
			for (int w = 0; w < commonWords.size(); w++){
				stringStatus.set(s, stringStatus.get(s).replace("@", ""));
				//stringStatus.set(s, stringStatus.get(s).replace("rt", ""));
				 stringStatus.set(s, stringStatus.get(s).toLowerCase().replace((" " + commonWords.get(w).toLowerCase() + " "), " "));
				 if (w == 0)
					 stringStatus.set(s, stringStatus.get(s).toLowerCase().replace((commonWords.get(w).toLowerCase() + " "), " "));
			}
		}
	}
	
	/**
	 * Gets Most Common word using a hashmap
	 */
	private void getCommonWords(){
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
	 * Prints out to console (is much easier to type)
	 * @param P Object to print
	 */
	private Object p(Object P){
		System.out.println(P); return P;
	}
}
