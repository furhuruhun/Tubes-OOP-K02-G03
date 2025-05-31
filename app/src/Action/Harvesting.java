package Action;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
import farm.FarmMap.Tile;
import items.Crops;
import items.Seeds; // Pastikan impor ini ada

public class Harvesting extends Action {
    int amountHarvested; // Tambahan: Variabel untuk jumlah hasil panen
    
    public Harvesting() {
        super(5, 5); // energyCost = 5, timeCost = 5
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        Location targetLocation = new Location(player.getLocation_infarm().getX() + 1, player.getLocation_infarm().getY());
        Tile targetTile = player.getFarmname().getFarmMap().getTile(targetLocation);

        //Cek ada tanaman di petak tersebut
        if (targetTile == null || !targetTile.isPlanted()) {
            System.out.println("Gak ada tanaman yang bisa di panen di sini.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        // informasi benih yang ditanam
        Seeds plantedSeed = targetTile.getPlantedSeed();

        if (!plantedSeed.isCompatibleWith(gameTime.getCurrSeason())) {
            System.out.println("Tanaman " + plantedSeed.getName() + " telah layu dan mati karena ga sesuai musim!");
            targetTile.clearTile(); // Hapus tanaman yang mati dari petak
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }            
            return;
        }

        if (!plantedSeed.isHarvestable(gameTime)) {
            System.out.println("Tanaman ini belum siap panen. Sabar boi, nunggu dulu sampe harinya nyampe");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        if (player.getEnergy() < -20 + this.energyCost) {
            System.out.println("Energi kamu tidak cukup untuk panen.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        // --- Proses Panen ---
        Crops harvestedCrop = plantedSeed.getCrops();
        amountHarvested = harvestedCrop.getJumlahperpanen();

        System.out.println("Kamu sudah memanen " + harvestedCrop.getName() + " (x" + amountHarvested + ")!");
        
        player.getInventory().addItem(harvestedCrop, amountHarvested);
        targetTile.clearTile(); // Mengembalikan petak menjadi kosong (atau menjadi 't' jika Anda ingin)
        
        player.setEnergy(player.getEnergy() - this.energyCost);
        gameTime.addTime(this.timeCostInMinute);

        // Pengecekan energi untuk tidur otomatis
        if (player.getEnergy() <= -20) {
            System.out.println("Kamu sudah tidak punya energi, kamu akan tidur sekarang.");
            Sleeping sleep = new Sleeping();
            sleep.perform(player, gameTime);
            System.out.println("Kamu sudah tidur");
        }
        try {
            Thread.sleep(1500); // Tambahkan jeda untuk melihat hasil panen
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    } 

    public int getAmountHarvested() {
        return amountHarvested; // Tambahan: Getter untuk jumlah hasil panen
    }
    public void setAmountHarvested(int amount) {
        this.amountHarvested = amount; // Tambahan: Setter untuk jumlah hasil panen
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
