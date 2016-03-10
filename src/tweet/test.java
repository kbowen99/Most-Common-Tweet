package tweet;

import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class test {
	
	ArrayList<Status> statuses = new ArrayList<>();
	HashMap<String, Integer> hmap = new HashMap<>();
	Twitter twitter = new TwitterFactory().getInstance();
	String user;
	
	public test(String user){
		this.user = user;
		loadTweets();
		p("Spamming Now");
		spamConsole();
		p("Spam Complete");
		sendTweet(user + " First Tweet:" + statuses.get(statuses.size() - 1).getText());
	}
	
	/**
	 * Writes all statuses in the arraylist 'statuses' to the console
	 */
	public void spamConsole(){
		for (Status s : statuses)
			p(s.getText());
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
		p(statuses.size() + " Magics Found");
	}
	
	/**
	 * Prints out to console (is much easier to type)
	 * @param P Object to print
	 */
	private void p(Object P){
		System.out.println(P);
	}
	
	
}
