package Juday;

import java.util.Scanner;

public class RideTheBusGame {
    private Deck deck = new Deck();
    private Scanner scanner = new Scanner(System.in);
    private int bet;

    public RideTheBusGame(int initialBet) {
        this.bet = initialBet;
    }

    public void play() {
        int multiplier = 1;
        Card first = deck.drawCard();
        System.out.println("Stage 1 - Red or Black?");
        System.out.print("Enter R for Red or B for Black: ");
        String guess = scanner.next().toUpperCase();
        boolean redGuess = guess.equals("R");

        if (first.isRed() == redGuess) {
            multiplier *= 2;
            System.out.println("Correct! Card: " + first);
        } else {
            System.out.println("Wrong! Card: " + first);
            System.out.println("You lost.");
            return;
        }

        Card second = deck.drawCard();
        System.out.println("Stage 2 - Higher or Lower?");
        System.out.print("Enter H for Higher or L for Lower: ");
        guess = scanner.next().toUpperCase();
        boolean higherGuess = guess.equals("H");

        if ((higherGuess && second.getValue() > first.getValue()) ||
            (!higherGuess && second.getValue() < first.getValue())) {
            multiplier *= 3;
            System.out.println("Correct! Card: " + second);
        } else {
            System.out.println("Wrong! Card: " + second);
            System.out.println("You lost.");
            return;
        }

        Card third = deck.drawCard();
        System.out.println("Stage 3 - Inside or Outside?");
        System.out.print("Enter I for Inside or O for Outside: ");
        guess = scanner.next().toUpperCase();
        int min = Math.min(first.getValue(), second.getValue());
        int max = Math.max(first.getValue(), second.getValue());
        boolean isInside = third.getValue() > min && third.getValue() < max;

        if ((guess.equals("I") && isInside) || (guess.equals("O") && !isInside)) {
            multiplier *= 5;
            System.out.println("Correct! Card: " + third);
        } else {
            System.out.println("Wrong! Card: " + third);
            System.out.println("You lost.");
            return;
        }

        Card fourth = deck.drawCard();
        System.out.println("Stage 4 - Guess the Suit");
        System.out.print("Enter suit (HEARTS, DIAMONDS, CLUBS, SPADES): ");
        guess = scanner.next().toUpperCase();

        if (fourth.getSuit().toString().equals(guess)) {
            multiplier *= 10;
            System.out.println("Correct! Card: " + fourth);
        } else {
            System.out.println("Wrong! Card: " + fourth);
            System.out.println("You lost.");
            return;
        }

        System.out.println("You won! Final payout: " + (multiplier * bet));
    }
}
