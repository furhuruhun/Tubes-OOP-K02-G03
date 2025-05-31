package Action;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import GameCalendar.Model.GameTime;
import GameCalendar.Model.Season;
import GameCalendar.Model.Weather;
import Player.Location;
import Player.Player;
import farm.FarmMap.Tile; // Pastikan impor ini ada dan benar
import items.Fish;
import items.FishDatabase;

public class Fishing extends Action {
    Fish selectedFish;
    private boolean isFishCaught;
    public Fishing() {
        super(5, 15);
        this.isFishCaught = false;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        this.isFishCaught = false;

        if (!player.haveitem("Fishing Rod")) { // Tambahan: Cek apakah pemain punya pancingan
            System.out.println("Kamu tidak punya pancingan!");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        if (player.getEnergy() < -20 + energyCost) {
            System.out.println("Energi tidak cukup untuk memancing.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        String location = player.getLocation_inworld();

        if (location.equals("Farm")) {
            Location playerLoc = player.getLocation_infarm();
            if (playerLoc == null) { // Tambahan: Pengecekan null untuk playerLoc
                System.out.println("Lokasi pemain di farm tidak diketahui.");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;
            }
            boolean dekatPond = false;
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
                if (tile != null && tile.getSymbol() == 'o') {
                    dekatPond = true;
                    break; 
                }
            }

            if (!dekatPond) { // Jika tidak ada petak 'o' di sekitar
                System.out.println("Kamu harus berada di dekat Pond (satu petak di sebelah 'o') untuk memancing di Farm.");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;
            }
        } else if (!(location.equals("Mountain Lake") || location.equals("Forest River") || location.equals("Ocean"))) {
            System.out.println("Kamu tidak berada di lokasi yang tepat untuk memancing (" + location + ").");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        player.setEnergy(player.getEnergy() - energyCost);
        gameTime.addTime(timeCostInMinute); //

        Season season = gameTime.getCurrSeason(); //
        int currentHour = gameTime.getCurrTime().getHour(); // Menggunakan getHour() untuk jam
        Weather weather = gameTime.getCurrWeather(); //

        // System.out.println("--- Mencoba Memancing ---");
        // System.out.println("Lokasi Pemain (dari player.getLocation_inworld()): " + location);
        // System.out.println("Musim Saat Ini (dari gameTime.getCurrSeason()): " + season);
        // System.out.println("Jam Saat Ini (dari gameTime.getCurrTime().getHour()): " + currentHour);
        // System.out.println("Cuaca Saat Ini (dari gameTime.getCurrWeather()): " + weather);

        List<Fish> possibleFish = FishDatabase.getFishFor(location, season, currentHour, weather); //
        if (possibleFish.isEmpty()) {
            System.out.println("Tidak ada ikan yang bisa ditangkap di lokasi ini pada waktu ini.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        clearScreen();
        selectedFish = possibleFish.get(new Random().nextInt(possibleFish.size()));
        System.out.println("Kamu mencoba menangkap ikan " + selectedFish.getName() + "..."); //

        int target = 0, maxAttempts = 0;
        switch (selectedFish.getFishType()) { //
            case "Common":
                target = new Random().nextInt(10) + 1; // Angka 1-10
                maxAttempts = 10; // Diberikan 10 percobaan
                break;
            case "Regular":
                target = new Random().nextInt(100) + 1; // Angka 1-100
                maxAttempts = 10; // Diberikan 10 percobaan
                break;
            case "Legendary":
                target = new Random().nextInt(500) + 1; // Angka 1-500
                maxAttempts = 7; // Diberikan 7 percobaan
                break;
            default: // Jika tipe ikan tidak dikenal
                System.out.println("Tipe ikan tidak dikenal: " + selectedFish.getFishType());
                return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Sebuah bayangan ikan mendekat!");
        if (selectedFish.getFishType().equals("Common")) {
            System.out.println("Ini adalah ikan Common. Tebak angka antara 1-10 untuk menangkapnya!");
            System.out.println("Kamu punya " + maxAttempts + " percobaan.");
        } else if (selectedFish.getFishType().equals("Regular")) {
            System.out.println("Ini adalah ikan Regular. Tebak angka antara 1-100 untuk menangkapnya!");
            System.out.println("Kamu punya " + maxAttempts + " percobaan.");
        } else if (selectedFish.getFishType().equals("Legendary")) {
            System.out.println("LUAR BIASA! Ini ikan LEGENDARY! Tebak angka antara 1-500!");
            System.out.println("Kamu hanya punya " + maxAttempts + " percobaan. Semoga berhasil!");
        }

        boolean caught = false;
        for (int i = 1; i <= maxAttempts; i++) {
            System.out.print("Percobaan ke " + i + ": ");
            if (!scanner.hasNextInt()) {
                System.out.println("Input tidak valid. Harap masukkan angka.");
                scanner.nextLine(); // Bersihkan buffer
                i--; // Ulangi percobaan saat ini
                continue;
            }
            int guess = scanner.nextInt();
            scanner.nextLine(); // Bersihkan newline character dari buffer setelah nextInt()

            if (guess == target) {
                System.out.println("Berhasil! Kamu mendapatkan ikan " + selectedFish.getName() + "!"); //
                player.addInventory(selectedFish, 1); //
                this.isFishCaught = true;
                caught = true;
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                break; 
            } else if (guess < target) {
                System.out.println("Terlalu kecil! Ikannya sedikit menjauh...");
            } else {
                System.out.println("Terlalu besar! Ikannya sedikit menjauh...");
            }
             // Tambahkan sedikit jeda atau variasi energi ikan jika diinginkan
            if (i < maxAttempts && Math.abs(guess - target) > (selectedFish.getFishType().equals("legendary") ? 50 : 20) ) { // Jika tebakan jauh
                System.out.println("Ikan itu tampak waspada!");
            }
        }
        
        if (!caught) {
            System.out.println("Sayang sekali, ikannya lolos.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    public Fish getSelectedFish() {
        return selectedFish;
    }
    public void setSelectedFish(Fish selectedFish) {
        this.selectedFish = selectedFish;
    }
    public boolean isFishFinallyCaught() {
        return isFishCaught;
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
