package Juday;

public class Card {
    public enum Suit { 
        HEARTS, DIAMONDS, CLUBS, SPADES 
    }
    private Suit suit;
    private int value; // 2–14 (11=J, 12=Q, 13=K, 14=A)

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() { 
        return suit; 
    }
    public int getValue() { 
        return value; 
    }

    @Override
    public String toString() {
        String[] face = { 
            "J", "Q", "K", "A" 
        };
        String val;
        if (value <= 10) {
            val = String.valueOf(value);
        } else {
            val = face[value - 11];
        }
        return val + " of " + suit;
    }

    public boolean isRed() {
        return suit == Suit.HEARTS || suit == Suit.DIAMONDS;
    }
}
