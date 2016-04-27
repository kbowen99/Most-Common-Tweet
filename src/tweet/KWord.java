package tweet;

/**
 * Represents a word, and the amount of times that time was found
 * is a better alternative to a hashmap
 * @author Kurtis Bowen
 */
public class KWord implements Comparable<KWord> {
	
	/**
	 * the word represented by this object
	 */
	private String word;
	
	/**
	 * the current count of the object
	 */
	private int count;
	
	/**
	 * Creates a new KWord Object with a set count
	 * @param word Represented Word
	 * @param count Starting Count
	 */
	public KWord(String word, int count){
		this.word = word.trim().toLowerCase();
		this.count = count;
	}
	
	/**
	 * Creates a new KWord Object with a default count of 1
	 * @param word Represented Word
	 */
	public KWord(String word){
		this.word = word.trim().toLowerCase();
		this.count = 1;
	}
	
	/**
	 * @return The current word
	 */
	public String getWord(){
		return this.word;
	}
	
	/**
	 * @return The current count
	 */
	public int getCount(){
		return this.count;
	}
	
	@Deprecated
	/**
	 * Changes the current word of the object, Should not be used
	 * @param word New Value of word
	 */
	public void setWord(String word){
		this.word = word;
	}
	
	@Deprecated
	/**
	 * Resets the value of count
	 * @param count
	 */
	public void setCount(int count){
		this.count = count;
	}
	
	/**
	 * Increments the current value of the current object
	 */
	public void increment(){
		this.count++;
	}
	
	/**
	 * Creates a string summarizing the KWord
	 */
	public String toString(){
		return "'" + this.word + "' Was Repeated " + this.count + " Times";
	}
	
	/**
	 * Used to Compare one KWord object to another KWord object based on its count.
	 * Allows for AutoMagical Sorting of an arraylist of KWords using Collections.sort().
	 * Is Definitely Magical
	 */
	@Override
	public int compareTo(KWord o) {
		return this.count - o.getCount();
	}
}
