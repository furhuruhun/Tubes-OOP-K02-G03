package Action;

import java.util.Scanner;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
import items.BuyableItems;
import items.Items;
import farm.FarmMap.Tile;

public class Selling extends Action {

    public Selling() {
        super(0, 15); // 15 menit waktu, tanpa energi
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if(player.getLocation_inworld().equals("Farm")) {
            Location playerLoc = player.getLocation_infarm();
            if (playerLoc == null) { // Tambahan: Pengecekan null untuk playerLoc
                System.out.println("Lokasi pemain di farm tidak diketahui.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return;
            }
            boolean dekatShippingBin = false;
            int px = playerLoc.getX();
            int py = playerLoc.getY();

            Location[] surroundingLocations = {
                // Arah Kardinal
                new Location(px + 1, py),     // Kanan
                new Location(px - 1, py),     // Kiri
                new Location(px, py + 1),     // Bawah
                new Location(px, py - 1),     // Atas
                // Arah Diagonal
                new Location(px + 1, py - 1), // diagonal
                new Location(px - 1, py - 1), 
                new Location(px + 1, py + 1), 
                new Location(px - 1, py + 1)  
            };

            // Iterasi untuk mengecek setiap lokasi sekitar
            for (Location loc : surroundingLocations) {
                Tile tile = player.getFarmname().getFarmMap().getTile(loc);
                if (tile != null && tile.getSymbol() == 's') {
                    dekatShippingBin = true;
                    break; 
                }
            }

            if (!dekatShippingBin) { // Jika tidak ada petak 's' di sekitar
                System.out.println("Kamu harus berada di dekat Shipping Bin (satu petak di sebelah 's') untuk memancing di Farm.");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return;
            }
        }
        clearScreen();   
        System.out.println("Silakan pilih item yang ingin dijual:");
        for(Items item : player.getInventory().getItemsMap().keySet()) {
            if(item instanceof items.BuyableItems){
                System.out.println(item.getName() + " : " + player.getInventory().getItemsMap().get(item) + " - " + ((BuyableItems)item).getHargaJual() + " gold / item");
            }
        }
        System.out.print("Masukkan nama item yang ingin dijual: ");
        Scanner scanner = new Scanner(System.in);
        String itemName = scanner.nextLine();
        BuyableItems itemToSell = null;
        for(Items item : player.getInventory().getItemsMap().keySet()) {
            if(item instanceof BuyableItems && item.getName().equalsIgnoreCase(itemName)) {
                itemToSell = (BuyableItems) item;
                break;
            }
        }
        if(itemToSell != null) {
            int itemCount = player.getInventory().getItemsMap().get(itemToSell);
            if(itemCount > 0) {
                System.out.print("Masukkan jumlah " + itemToSell.getName() + " yang ingin dijual (tersedia: " + itemCount + "): ");
                int quantity = scanner.nextInt();
                if(quantity > 0 && quantity <= itemCount) {
                    player.getInventory().removeItem(itemToSell, quantity);
                    player.setGold(player.getGold() + ((BuyableItems)itemToSell).getHargaJual() * quantity);
                    System.out.println("Anda telah menjual " + quantity + " " + itemToSell.getName() + ". Gold Anda sekarang: " + player.getGold());
                } else {
                    System.out.println("Jumlah yang dimasukkan tidak valid.");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return;
                }
            } else {
                System.out.println("Anda tidak memiliki " + itemToSell.getName() + " di inventory.");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return;
            }
        } else {
            System.out.println("Item tidak ditemukan di inventory.");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return;
        }
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        gameTime.addTime(timeCostInMinute);
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