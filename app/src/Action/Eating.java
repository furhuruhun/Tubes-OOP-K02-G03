package Action;

import java.util.HashMap;
import java.util.Scanner;

import GameCalendar.Model.GameTime;
import Player.Player;
import items.Fish;
import items.Food;
import items.Items;

public class Eating extends Action {
    public Eating() {
        super(0, 5);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if (player.getEnergy() >= 100) {
            System.out.println("Energi sudah penuh.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        // cek apabila tidak ada items bertipe Food
        // di dalam inventory
        boolean adaMakanan = false;
        for (Items barang : player.getInventory().getItemsMap().keySet()) {
            if (barang instanceof Food || barang instanceof Fish) {
                adaMakanan = true;
                break;
            }
        }
        if (!adaMakanan) {
            System.out.println("Tidak ada makanan di inventory.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        for (Items barang : player.getInventory().getItemsMap().keySet()) {
            clearScreen();
            if (barang instanceof Food || barang instanceof Fish) {
                System.out.println("List makanan di inventory:");
                int i = 1;
                HashMap<Items, Integer> foodList = new HashMap<>();
                for (Items item : player.getInventory().getItemsMap().keySet()) {
                    if ((item instanceof Food || item instanceof Fish) && player.getInventory().getItemsMap().get(item) > 0) {
                        System.out.println(i + ". " + item.getName() + " : " + player.getInventory().getItemsMap().get(item));
                        foodList.put((Items) item, player.getInventory().getItemsMap().get(item));
                        i++;
                    }
                }
                Scanner scanner = new Scanner(System.in);
                System.out.print("Pilih makanan yang ingin dimakan (1 - " + foodList.size() + "): ");
                int input = scanner.nextInt();
                boolean validInput = false;
                while (!validInput) {
                    if (input >= 1 && input <= foodList.size()) {
                        validInput = true;
                    } else {
                        System.out.print("Input tidak valid. Silakan pilih makanan yang ingin dimakan (1 - " + foodList.size() + "): ");
                        input = scanner.nextInt();
                    }
                }
                Items selectedFood = (Items) foodList.keySet().toArray()[input - 1];
                System.out.println("Anda telah memilih " + selectedFood.getName() + ".");
                if(selectedFood instanceof Food){
                    Food foood = (Food)selectedFood;
                    int energyGained = foood.getEnergy();
                    player.setEnergy(player.getEnergy() + energyGained);
                    System.out.println("Anda telah makan " + selectedFood.getName() + " dan mendapatkan " + energyGained + " energi.");
                }
                else if(selectedFood instanceof Fish) {
                    player.setEnergy(player.getEnergy() + 1);
                    int energyGained = 1;
                    System.out.println("Anda telah makan " + selectedFood.getName() + " dan mendapatkan " + energyGained + " energi.");
                }
                if(player.getEnergy() > 100) {
                    player.setEnergy(100);
                }
                gameTime.addTime(5);
                player.getInventory().removeItem(selectedFood, 1);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                break;
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