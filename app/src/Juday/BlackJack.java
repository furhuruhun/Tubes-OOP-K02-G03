package Juday;

import java.util.Scanner;
import Player.Player;

public class BlackJack {
    private Deck deck;
    private SetUp player;
    private SetUp dealer;
    private Scanner scanner;

    public BlackJack() {
        deck = new Deck();
        player = new SetUp();
        dealer = new SetUp();
        scanner = new Scanner(System.in);
    }

    public void play(Player a) {
        System.out.print("Masukkan bet: ");
        int bet = scanner.nextInt();
        scanner.nextLine();

        // Bagi 2 kartu ke player dan dealer
        player.addCard(deck.drawCard());
        player.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());

        System.out.println("\nKartu Pemain:");
        player.printInitial(true);
        System.out.println("Total: " + player.getInitialValue());

        System.out.println("\nKartu Dealer:");
        dealer.printInitial(false); // hanya satu yang ditampilkan

        // Giliran pemain
        while (true) {
            System.out.print("\nPilih [hit/stand]: ");
            String choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("hit")) {
                player.addCard(deck.drawCard());
                System.out.println("\nKartu Pemain:");
                player.printInitial(true);
                System.out.println("Total: " + player.getInitialValue());

                if (player.isBusted()) {
                    System.out.println("Busted! Kamu kalah.");
                    return;
                }
            } else if (choice.equalsIgnoreCase("stand")) {
                break;
            }
        }

        // Giliran dealer
        System.out.println("\nKartu Dealer:");
        dealer.printInitial(true);
        System.out.println("Total: " + dealer.getInitialValue());

        while (dealer.getInitialValue() < 17) {
            System.out.println("Dealer menarik kartu...");
            dealer.addCard(deck.drawCard());
            dealer.printInitial(true);
            System.out.println("Total: " + dealer.getInitialValue());
        }

        // Penentuan hasil
        if (dealer.isBusted() || player.getInitialValue() > dealer.getInitialValue()) {
            System.out.println("Selamat! Kamu menang. Uang kamu menjadi " + (bet * 2));
            a.setGold(a.getGold() + bet * 2);
        } else if (player.getInitialValue() < dealer.getInitialValue()) {
            System.out.println("Kamu kalah.");
            a.setGold(a.getGold() - bet);
        } else {
            System.out.println("Seri. Uang kamu kembali: " + bet);
            a.setGold(a.getGold() + bet);
        }
    }
}
