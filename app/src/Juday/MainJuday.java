package Juday;

import Player.Player;

import java.util.Scanner;

public class MainJuday {
    public void start(Player player) {
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("Lesgow juday: mau taruhan brp brok? ");
            int bet = input.nextInt();
            input.nextLine();
            if(bet < player.getGold()) {
                System.out.println("Taruhan diterima: " + bet);
            } else {
                System.out.println("Uang kamu tidak cukup untuk taruhan ini. Silakan coba lagi.");
                continue;
            }
            System.out.println("Mau bersenang senang di game mana nih? (Slot/RTB/BlackJack)");
            String type = input.nextLine();

            if (type.equalsIgnoreCase("Slot")) {
                //kalo milih slot
                SlotMachine machine = new SlotMachine();
                Symbol[] result = machine.spin();
                System.out.println("Hasil spin:");
                for (Symbol s : result) {
                    System.out.print(s + " ");
                }
                int payout = machine.calculatePayout(result, bet);
                if (payout > 0) {
                    player.setGold(player.getGold() + payout);
                    System.out.println("\nKamu menang! Payout: " + payout);
                } else {
                    player.setGold(player.getGold() - bet);
                    System.out.println("\nKamu kalah. Taruhan hilang: " + bet);
                }
                System.out.println("\nPayout: " + payout);
            }
            else if(type.equalsIgnoreCase("RTB")) {
                //kalo milih ride the bus
                RideTheBusGame game = new RideTheBusGame(bet);
                game.play(player);
            }
            else if(type.equalsIgnoreCase("BlackJack")) {
                //kalo milih main blekjek
                BlackJack poker = new BlackJack();
                poker.play(player);
            }
            else {
                System.out.println("Game tidak dikenal. Silakan pilih Slot, RTB, atau BlackJack.");
            }

            // Konfirmasi setelah main
            boolean valid = false;
            while (!valid) {
                System.out.println("\nMau keluar (exit), lanjut main game ini (lanjut), atau ganti game (ganti)?");
                String next = input.nextLine().trim().toLowerCase();
                if (next.equals("exit") || next.equals("keluar")) {
                    running = false;
                    valid = true;
                } else if (next.equals("lanjut")) {
                    valid = true;
                    // do nothing, loop will repeat with same game
                } else if (next.equals("ganti")) {
                    valid = true;
                    // loop will repeat and ask for new game
                } else {
                    System.out.println("Input tidak valid. Pilih: exit, lanjut, atau ganti.");
                }
            }
        }
    }
    private void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                for (int i = 0; i < 50; ++i) System.out.println();
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}