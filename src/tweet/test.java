package tweet;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class test {
	
	ArrayList<Status> statuses = new ArrayList<>();
	Twitter twitter = new TwitterFactory().getInstance();
	String user;
	
	public test(String user, boolean limit){
		this.user = user;
		p("Magic Finding");
		int pageno = 1;
		boolean complete = false;
		while (!complete) {
		  try {
		    int size = statuses.size(); 
		    Paging page = new Paging(pageno++, 100);
		    statuses.addAll(twitter.getUserTimeline(user, page));
		    if (statuses.size() == size || (statuses.size() >= 2000 && limit))
		    	complete = true;
		  }
		  catch(TwitterException e) {
		    e.printStackTrace();
		  }
		}
		p(statuses.size() + " Magics Found");
		p("Spamming Now");
		spamConsole();
		p("Spam Complete");
		String tmp = user + " First Tweet:" + statuses.get(statuses.size() - 1).getText();
		sendTweet(((tmp.length() > 140) ? tmp.substring(0,140) : tmp));
	}
	
	public void spamConsole(){
		for (Status s : statuses)
			p(s.getText());
	}
	
	public void sendTweet(String s){
		try {
			twitter.updateStatus(s);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}
	
	private void p(Object P){
		System.out.println(P);
	}
	
}
