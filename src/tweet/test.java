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
	
	public test(String user){
		this.user = user;
		p("Magic Finding");
		int pageno = 1;
		boolean complete = false;
		while (!complete) {
		  try {
		    int size = statuses.size(); 
		    Paging page = new Paging(pageno++, 100);
		    statuses.addAll(twitter.getUserTimeline(user, page));
		    if (statuses.size() == size)
		    	complete = true;
		  }
		  catch(TwitterException e) {
		    e.printStackTrace();
		  }
		}
		p("Magic Found");
	}
	
	private void p(Object P){
		System.out.println(P);
	}
	
}
