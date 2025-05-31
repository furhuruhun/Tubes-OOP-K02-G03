package Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
import items.Items;
import items.Seeds;

public class Planting extends Action {
    public Planting(){
        super(5, 5); // energyCost = 5, timeCost = 5
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        
        List<Seeds> plantableSeeds = new ArrayList<>();
        Map<Items, Integer> inventoryMap = player.getInventory().getItemsMap();

        for (Items item : inventoryMap.keySet()) {
            if (item instanceof Seeds) {
                Seeds seed = (Seeds) item;
                // Cek apakah benih cocok dengan musim saat ini
                if (seed.isCompatibleWith(gameTime.getCurrSeason())) {
                    plantableSeeds.add(seed);
                }
            }
        }

        if (plantableSeeds.isEmpty()) {
            System.out.println("Kamu tidak punya benih yang bisa ditanam di musim ini.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        Location targetLocation = new Location(player.getLocation_infarm().getX() + 1, player.getLocation_infarm().getY());

        if (!player.getFarmname().getFarmMap().isTillable(targetLocation)) {
            System.out.println("Gak bisa ditanam disini, tanahnya belum di tilling.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        if (player.getEnergy() < -20 + this.energyCost) {
            System.out.println("Gak punya cukup energi untuk menanam.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        System.out.println("Benih yang bisa ditanam di musim ini:");
        for (Seeds seed : plantableSeeds) {
            System.out.println("- " + seed.getName() + " (Jumlah: " + inventoryMap.get(seed) + ")");
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Masukkan nama benih yang ingin kamu tanam (atau ketik 'X'): ");
        String seedName = scanner.nextLine();

        if (seedName.equalsIgnoreCase("X")) {
            System.out.println("Menanam dibatalkan.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        Seeds seedToPlant = null;
        for (Seeds seed : plantableSeeds) {
            if (seed.getName().equalsIgnoreCase(seedName)) {
                seedToPlant = seed;
                break;
            }
        }
        
        if (seedToPlant != null) {
            player.getFarmname().getFarmMap().getTile(targetLocation).setPlantedSeed(seedToPlant);
            player.getInventory().removeItem(seedToPlant, 1); // Hapus 1 benih dari inventory
            
            player.setEnergy(player.getEnergy() - this.energyCost);
            gameTime.addTime(this.timeCostInMinute);
            
            System.out.println("Kamu sudah menanam " + seedToPlant.getName());

            // cek biar bisa otomatis tidur
            if (player.getEnergy() <= -20) {
                System.out.println("Kamu sudah tidak punya energi, kamu akan tidur sekarang.");
                Sleeping sleep = new Sleeping();
                sleep.perform(player, gameTime);
                System.out.println("Kamu sudah tidur");
            }
        } else {
            System.out.println("Tidak ada benih dengan nama '" + seedName + "' yang bisa ditanam saat ini.");
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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