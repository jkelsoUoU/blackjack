package blackJack;

import java.util.ArrayList;

public class Hand 
{
	//fields
	private int handIndex; //Order of play
	private ArrayList<Card> inHand = new ArrayList<Card>(2); //Default arraylist of 2 cards. Empty
	private int handValue; //Value of hand
	private boolean bust; //Bust or not
	private boolean playing; //playing or not. set to true then false if bust or hand held
	
	//Constructor
	public Hand(int hi, Card c1, Card c2) {
		handIndex = hi;
		inHand.add(c1);
		inHand.add(c2);
		playing = true;
	}
	
	//Getters
	public int getHandIndex() {
		return handIndex;
	}
	public ArrayList<Card> getInHand() {
		return inHand;
	}
	public int getHandValue() {
		return handValue;
	}
	public boolean getBust() {
		return bust;
	}	
	
	public boolean getPlaying()	{
		return playing;
	}
	
	//Methods
	public void setBust() {
		if (handValue > 21)	{
				bust = true;
				playing = false;
		}
		else bust = false;
	}

	public void setPlaying(boolean b) {
			this.playing = b;
	}
	
	@Override
	public String toString() {
		String handString = "Hand \n";
		for(Card el: inHand)
			handString += el.toString() + "\n";
		handString += " Hand Value of " + getHandValue() + "bust: " + getBust();
		return handString;
	}

	public int setHandValue() {
		handValue = 0;
		boolean hasAce = false;
		for (Card el : inHand) {
				handValue += el.getValue();
				if (el.getRank() == Rank.ACE) hasAce = true;
		}
		if (handValue + 10 <= 21 && hasAce) handValue +=10; //adds 10 to hand value if there's an ace and wont bust 
		return handValue;		
	}
	
	public void holdHand() {
		playing = false;
	}
	
	public void hitHand(Card c)	{
		if(!getBust()) {
			this.inHand.add(c);
			this.setHandValue();
			this.setBust();
		}
	}
	
}
