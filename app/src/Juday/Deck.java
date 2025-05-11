package Juday;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;
    private Random rand = new Random();

    public Deck() {
        cards = new ArrayList<>();
        Card.Suit[] suits = Card.Suit.values();

        // Loop setiap suit 
        for (int i = 0; i < suits.length; i++) {
            Card.Suit suit = suits[i];

            // Loop melalui nilai kartu dari 2 hingga 14
            for (int val = 2; val <= 14; val++) {
                cards.add(new Card(suit, val));
            }
        }

        Collections.shuffle(cards, rand);
    }


    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }
}

