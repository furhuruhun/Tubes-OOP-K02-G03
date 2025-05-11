package Juday;

import java.util.Scanner;
public class MainJuday {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Lesgow juday: mau taruhan brp brok? ");
        int bet = input.nextInt();
        input.nextLine();
        System.out.println("Mau bersenang senang di game mana nih? (Slot/RTB/BlackJack)");
        String type = input.nextLine();

        if (type.equals("Slot")) {
            //kalo milih slot
            SlotMachine machine = new SlotMachine();
            Symbol[] result = machine.spin();
            System.out.println("Hasil spin:");
            for (Symbol s : result) {
                System.out.print(s + " ");
            }
            int payout = machine.calculatePayout(result, bet);
            System.out.println("\nPayout: " + payout);
        }

        else if(type.equals("RTB")) {
            //kalo milih ride the bus
            RideTheBusGame game = new RideTheBusGame(bet);
            game.play();
        }

        else if(type.equals("BlackJack")) {
            //kalo milih main blekjek
            BlackJack poker = new BlackJack();
            poker.play();
        }
        input.close();
    }
}


