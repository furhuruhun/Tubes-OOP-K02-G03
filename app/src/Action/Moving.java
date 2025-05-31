package Action;

import java.util.Scanner;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
import farm.FarmMap.Tile; 

public class Moving extends Action {
    private char temp; 

    public Moving() {
        super(0, 0); 
        this.temp = '.'; 
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if(!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Anda ga lagi di farm huuuu :(");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        Scanner scanner = new Scanner(System.in); 
        while (true) {
            clearScreen();
            player.getFarmname().getFarmMap().printMap(); 
            System.out.println("\nLokasi saat ini: (" + player.getLocation_infarm().getX() + ", " + player.getLocation_infarm().getY() + ")");
            System.out.println("Tanah di bawah mu saat ini: '" + temp + "'"); 
            System.out.println("Ketik arah tujuan (w, a, s, d). 'x' untuk keluar.");
            System.out.println("w = atas, a = kiri, s = bawah, d = kanan, x = keluar");

            String input = scanner.nextLine().trim().toLowerCase();

            if (input.isEmpty()) {
                continue; 
            }
            
            if (input.equals("x")) {
                System.out.println("Keluar dari mode bergerak.");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return; 
            }

            Location currentLocation = player.getLocation_infarm();
            Tile currentTile = player.getFarmname().getFarmMap().getTile(currentLocation);

            if (currentTile == null) {
                System.out.println("Kesalahan: Tidak bisa mendapatkan tile di lokasi pemain saat ini. ("+ currentLocation.getX() + "," + currentLocation.getY() +")");
                try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return; 
            }

            Location newLocation = null;
            int newX = currentLocation.getX();
            int newY = currentLocation.getY();

            switch (input) {
                case "w":
                    newY--;
                    break;
                case "a":
                    newX--;
                    break;
                case "s":
                    newY++;
                    break;
                case "d":
                    newX++;
                    break;
                default:
                    System.out.println("Input tidak valid. Gunakan w, a, s, d, atau x.");
                    try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    continue;
            }
            newLocation = new Location(newX, newY);

            Tile targetTile = player.getFarmname().getFarmMap().getTile(newLocation);

            if (targetTile == null) {
                // di luar matrix nya
                System.out.println("Tidak bisa bergerak ke luar peta atau ke lokasi tidak valid!");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            } else {
                char symboltarget =  targetTile.getSymbol();
                if(symboltarget == 'h' || symboltarget == 'o' || symboltarget == 's') {
                    String tipeObstacle = "";
                    if (symboltarget == 'h') tipeObstacle = "Rumah";
                    else if (symboltarget == 'o') tipeObstacle = "Kolam (Pond)";
                    else if (symboltarget == 's') tipeObstacle = "Shipping Bin";
                    System.out.println("Tidak bisa bergerak ke lokasi tersebut, ada " + tipeObstacle + "!");   
                    try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }  
                } else {
                    currentTile.setSymbol(this.temp);
                    this.temp = targetTile.getSymbol(); 
                    targetTile.setSymbol('p');
                    player.setLocation_infarm(newLocation);
                }
            }
        }
    }

    public char getTemp() {
        return temp;
    }

    public void setTemp(char temp) {
        this.temp = temp;
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
