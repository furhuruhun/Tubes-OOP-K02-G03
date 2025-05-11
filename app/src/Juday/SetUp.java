package Juday;

import java.util.ArrayList;
import java.util.List;

public class SetUp {
    protected List<Card> initial;

    public SetUp() {
        initial = new ArrayList<>();
    }

    public void addCard(Card card) {
        initial.add(card);
    }

    public List<Card> getInitial() {
        return initial;
    }

    public int getInitialValue() {
        int total = 0;
        int aceCount = 0;

        for (Card card : initial) {
            int value = card.getValue();
            if (value >= 11 && value <= 13) {
                total += 10; // Jack, Queen, King
            } else if (value == 14) {
                total += 11; // Ace awalnya 11
                aceCount++;
            } else {
                total += value;
            }
        }

        // Jika total > 21 dan ada Ace, ubah Ace dari 11 jadi 1
        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }

    public boolean isBusted() {
        return getInitialValue() > 21;
    }

    public void printInitial(boolean showAll) {
        for (int i = 0; i < initial.size(); i++) {
            if (!showAll && i == 1) {
                System.out.println("[Hidden Card]");
            } else {
                System.out.println(initial.get(i));
            }
        }
    }
}

