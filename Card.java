package blackJack;

public class Card
{
	//fields
	private final Suit suit;
	private final Rank rank;
	private int value;
	private final int id;
	
	//ctors
	public Card(Suit s, Rank r, int v, int i) {
		suit = s;
		rank = r;
		value = v;
		id = i;
	}
	
	//methods
	@Override
	public String toString() {
		return String.format("%s of %s %d %d", rank, suit, value, id);
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int i)	{
		value = i;
	}

	public Suit getSuit() {
		return suit;
	}

	public Rank getRank() {
		return rank;
	}

	public int getId() {
		return id;
	}
}
