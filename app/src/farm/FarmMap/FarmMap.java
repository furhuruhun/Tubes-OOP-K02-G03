package farm.FarmMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import House.House;
import Player.Location;
import Pond.Pond;
import ShippingBin.ShippingBin;

public class FarmMap {
    private static final int width = 32;
    private static final int height = 32;
    private House house;
    private Pond pond;
    private ShippingBin shippingBin;
    private Map<Location, Tile> tiles;
    private Random random = new Random();

    public FarmMap(House house, Pond pond, ShippingBin shippingBin) {
        this.house = house;
        this.pond = pond;
        this.shippingBin = shippingBin;
        this.tiles = new HashMap<>();
    }

    public Tile getTile(Location loc) {
        if (loc == null || loc.getX() < 0 || loc.getX() >= width || loc.getY() < 0 || loc.getY() >= height) {
            return null;
        }
        return tiles.get(loc);
    }

    public ShippingBin getShippingBin() {
        return shippingBin;
    }

    public Pond getPond() {
        return pond;
    }


    public House getHouse() {
        return house;
    }


    public Map<Location, Tile> getTiles() {
        return this.tiles;
    }

    public boolean canPlaceStructure(int x, int y, int w, int h) {
        if (x < 0 || y < 0 || x + w > width || y + h > height) {
            return false; // Struktur di luar batas peta
        }
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Location loc = new Location(x + j, y + i);
                Tile tile = tiles.get(loc);
                if (tile == null || tile.getSymbol() != '.') { // Tile harus ada dan kosong
                    return false;
                }
            }
        }
        return true;
    }

    public void placeStructure(int x, int y, int w, int h, char symbol) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Location loc = new Location(x + j, y + i);
                Tile tile = tiles.get(loc);
                if (tile != null) {
                    tile.setSymbol(symbol);
                }
            }
        }
    }

    public void generateMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Location loc = new Location(j, i);
                tiles.put(loc, new Tile());
            }
        }
    }


    public void placeRandomHouse() {
        int houseActualWidth = House.DEFAULT_WIDTH;
        int houseActualHeight = House.DEFAULT_HEIGHT;
        int attempts = 100;

        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = random.nextInt(width - houseActualWidth + 1);
            int y = random.nextInt(height - houseActualHeight + 1);

            if (canPlaceStructure(x, y, houseActualWidth, houseActualHeight)) {
                placeStructure(x, y, houseActualWidth, houseActualHeight, 'h');
                if (this.house != null) {
                    this.house.setLocation(new Location(x, y));
                }
                return;
            }
        }
        System.err.println("Gagal menempatkan House secara acak setelah " + attempts + " percobaan. Menempatkan di lokasi default (1,1) jika memungkinkan.");
        if (canPlaceStructure(1, 1, houseActualWidth, houseActualHeight)) { 
            placeStructure(1, 1, houseActualWidth, houseActualHeight, 'h');
            if (this.house != null) {
                this.house.setLocation(new Location(1, 1));
            }
        } else {
             System.err.println("Gagal menempatkan House di lokasi default.");
        }
    }


    public void placeRandomPond() {
        int pondWidth = 4;
        int pondHeight = 3;
        int attempts = 100;

        for (int attempt = 0; attempt < attempts; attempt++) {
            int x = random.nextInt(width - pondWidth + 1);
            int y = random.nextInt(height - pondHeight + 1);

            if (canPlaceStructure(x, y, pondWidth, pondHeight)) {
                placeStructure(x, y, pondWidth, pondHeight, 'o');
                if (this.pond != null) {
                    this.pond.setLocation(new Location(x, y));
                }
                return;
            }
        }
        System.err.println("Gagal menempatkan Pond secara acak setelah " + attempts + " percobaan.");
    }

    public void placeRandomShippingBin() {
        if (this.house == null || this.house.getLocation() == null) {
            System.err.println("Tidak bisa menempatkan Shipping Bin: lokasi Rumah tidak diketahui/belum ditempatkan.");
            return;
        }

        int shipWidth = 3;
        int shipHeight = 2;

        Location houseLoc = this.house.getLocation();
        int houseActualWidth = House.DEFAULT_WIDTH;
        int houseActualHeight = House.DEFAULT_HEIGHT; // Digunakan untuk penyejajaran vertikal

        int targetX = houseLoc.getX() + houseActualWidth + 1;

        int targetY1 = houseLoc.getY();
        if (canPlaceStructure(targetX, targetY1, shipWidth, shipHeight)) {
            placeStructure(targetX, targetY1, shipWidth, shipHeight, 's');
            if (this.shippingBin != null) {
                this.shippingBin.setLocation(new Location(targetX, targetY1));
            }
            return; 
        }

        int houseMiddleY = houseLoc.getY() + houseActualHeight / 2;
        int targetY2 = houseMiddleY - shipHeight / 2;
        if (canPlaceStructure(targetX, targetY2, shipWidth, shipHeight)) {
            placeStructure(targetX, targetY2, shipWidth, shipHeight, 's');
            if (this.shippingBin != null) {
                this.shippingBin.setLocation(new Location(targetX, targetY2));
            }
            return; 
        }
        
        int targetY3 = houseLoc.getY() + houseActualHeight - shipHeight;
         if (canPlaceStructure(targetX, targetY3, shipWidth, shipHeight)) {
            placeStructure(targetX, targetY3, shipWidth, shipHeight, 's');
            if (this.shippingBin != null) {
                this.shippingBin.setLocation(new Location(targetX, targetY3));
            }
            return; // Berhasil ditempatkan
        }


        int fallbackY_down = targetY1 + 1;
        if (fallbackY_down + shipHeight <= height && canPlaceStructure(targetX, fallbackY_down, shipWidth, shipHeight)) {
             placeStructure(targetX, fallbackY_down, shipWidth, shipHeight, 's');
             if (this.shippingBin != null) {
                 this.shippingBin.setLocation(new Location(targetX, fallbackY_down));
             }
             return; 
        }

        int fallbackY_up = targetY1 - 1;
        if (fallbackY_up >=0 && canPlaceStructure(targetX, fallbackY_up, shipWidth, shipHeight)) {
             placeStructure(targetX, fallbackY_up, shipWidth, shipHeight, 's');
             if (this.shippingBin != null) {
                 this.shippingBin.setLocation(new Location(targetX, fallbackY_up));
             }
             return; 
        }

        System.err.println("Gagal menempatkan Shipping Bin di sebelah kanan rumah setelah beberapa upaya. Pastikan ada ruang kosong yang cukup.");
        System.err.println("Detail upaya: targetX=" + targetX + ", houseY=" + houseLoc.getY() + 
                           ", houseWidth=" + houseActualWidth + ", houseHeight=" + houseActualHeight +
                           ", shipWidth=" + shipWidth + ", shipHeight=" + shipHeight);
    }

    public void placePlayer(Location loc) {
        Tile targetTile = getTile(loc);
        if (targetTile != null && targetTile.getSymbol() == '.') {
            targetTile.setSymbol('p');
        }
    }


    public void placePlayerforMoving(Location loc) {
        Tile targetTile = getTile(loc);
        if (targetTile != null) {
            targetTile.setSymbol('p');
        } else {
            System.out.println("Tidak bisa bergerak ke lokasi di luar map.");
        }
    }


    public void printMap() {
        if (tiles == null || tiles.isEmpty()) {
            System.out.println("Peta belum di-generate.");
            return;
        }
        System.out.println("\n=== PETA FARM ===");
        for (int i = 0; i < height; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < width; j++) {
                Location loc = new Location(j, i);
                Tile tile = tiles.get(loc); 
                if (tile != null) {
                    if (tile.getSymbol() == 'p') { 
                        row.append('p').append(" ");
                    } else if (tile.isPlanted() && tile.getPlantedSeed() != null) {
                        row.append('l').append(" ");
                    } else {
                        row.append(tile.getSymbol()).append(" ");
                    }
                } else {
                    row.append("? "); // Seharusnya tidak terjadi jika generateMap dipanggil
                }
            }
            System.out.println(row.toString().trim());
        }
        System.out.println("Legenda: p (Player), h (House), o (Pond), s (Shipping Bin)");
        System.out.println("         . (Tillable), t (Tilled), l (Planted)");
    }

    public boolean isEdgeTile(Location loc) {
        if (loc == null) return false;
        return loc.getX() == 0 || loc.getX() == width - 1 || loc.getY() == 0 || loc.getY() == height - 1;
    }

    public boolean isEmpty(Location loc) {
        Tile tile = getTile(loc);
        return tile != null && tile.getSymbol() == '.';
    }

    public boolean isTillable(Location loc) {
        Tile tile = getTile(loc);
        return tile != null && tile.getSymbol() == 't';
    }


    public boolean isPlanted(Location loc) {
        Tile tile = getTile(loc);
        return tile != null && tile.isPlanted();
    }


    public boolean isPlayerInHouse(Location playerLoc) {
        if (house == null || house.getLocation() == null || playerLoc == null) {
            return false;
        }

        Location houseStartLoc = house.getLocation();
        int houseEndX = houseStartLoc.getX() + House.DEFAULT_WIDTH;
        int houseEndY = houseStartLoc.getY() + House.DEFAULT_HEIGHT;

        if (playerLoc.getX() >= houseStartLoc.getX() && playerLoc.getX() < houseEndX &&
            playerLoc.getY() >= houseStartLoc.getY() && playerLoc.getY() < houseEndY) {

            return true;
        }
    

        if (playerLoc.getX() == 0 && playerLoc.getY() == 0) { //pojok kiri atas
            Tile t1 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()+1));
            Tile t2 = getTile(new Location(playerLoc.getX(), playerLoc.getY()+1));
            Tile t3 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h');
        }

        if (playerLoc.getX() == 31 && playerLoc.getY() == 0) { // pojok kanan atas
            Tile t1 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()));
            Tile t2 = getTile(new Location(playerLoc.getX(), playerLoc.getY()+1));
            Tile t3 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()+1));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h');
        }

        if (playerLoc.getX() == 0 && playerLoc.getY() == 31) { // pojok kiri bawah 
            Tile t1 = getTile(new Location(playerLoc.getX(), playerLoc.getY()-1));
            Tile t2 = getTile(new Location(playerLoc.getX() + 1, playerLoc.getY()));
            Tile t3 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()- 1));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h');
        }

        if (playerLoc.getX() == 31 && playerLoc.getY() == 31) { // pojok kanan bawah
            Tile t1 = getTile(new Location(playerLoc.getX(), playerLoc.getY()-1));
            Tile t2 = getTile(new Location(playerLoc.getX() - 1, playerLoc.getY()));
            Tile t3 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()-1));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h');
        }

        if (playerLoc.getX() == 0) { // di sisi kiri
            Tile t1 = getTile(new Location(playerLoc.getX(), playerLoc.getY()-1));
            Tile t2 = getTile(new Location(playerLoc.getX(), playerLoc.getY()+1));
            Tile t3 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()-1));
            Tile t4 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()+1));
            Tile t5 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h') ||
                   (t4 != null && t4.getSymbol() == 'h') ||
                   (t5 != null && t5.getSymbol() == 'h');
        }

        if (playerLoc.getX() == 31) { // sisi kanan
            Tile t1 = getTile(new Location(playerLoc.getX(), playerLoc.getY()+1));
            Tile t2 = getTile(new Location(playerLoc.getX(), playerLoc.getY()-1));
            Tile t3 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()));
            Tile t4 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()+1));
            Tile t5 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()-1));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h') ||
                   (t4 != null && t4.getSymbol() == 'h') ||
                   (t5 != null && t5.getSymbol() == 'h');
        }

        if (playerLoc.getY() == 0) { //atas
            Tile t1 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()));
            Tile t2 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()));
            Tile t3 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()+1)); 
            Tile t4 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()+1));
            Tile t5 = getTile(new Location(playerLoc.getX(), playerLoc.getY()+1));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h') ||
                   (t4 != null && t4.getSymbol() == 'h') ||
                   (t5 != null && t5.getSymbol() == 'h');
        }

        if(playerLoc.getY() == 31) { //bawah
            Tile t1 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()));
            Tile t2 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()));
            Tile t3 = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()-1));
            Tile t4 = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()-1));
            Tile t5 = getTile(new Location(playerLoc.getX(), playerLoc.getY()-1));
            return (t1 != null && t1.getSymbol() == 'h') ||
                   (t2 != null && t2.getSymbol() == 'h') ||
                   (t3 != null && t3.getSymbol() == 'h') ||
                   (t4 != null && t4.getSymbol() == 'h') ||
                   (t5 != null && t5.getSymbol() == 'h');
        }
        else { 
            Tile t_bawah = getTile(new Location(playerLoc.getX(), playerLoc.getY()+1));
            Tile t_kanan = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()));
            Tile t_atas = getTile(new Location(playerLoc.getX(), playerLoc.getY()-1));
            Tile t_kiri = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()));
            Tile t_kanan_bawah = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()+1));
            Tile t_kiri_atas = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()-1));
            Tile t_kanan_atas = getTile(new Location(playerLoc.getX()+1, playerLoc.getY()-1));
            Tile t_kiri_bawah = getTile(new Location(playerLoc.getX()-1, playerLoc.getY()+1));
            
            return (t_bawah != null && t_bawah.getSymbol() == 'h') ||
                   (t_kanan != null && t_kanan.getSymbol() == 'h') ||
                   (t_atas != null && t_atas.getSymbol() == 'h') ||
                   (t_kiri != null && t_kiri.getSymbol() == 'h') ||
                   (t_kanan_bawah != null && t_kanan_bawah.getSymbol() == 'h') ||
                   (t_kiri_atas != null && t_kiri_atas.getSymbol() == 'h') ||
                   (t_kanan_atas != null && t_kanan_atas.getSymbol() == 'h') ||
                   (t_kiri_bawah != null && t_kiri_bawah.getSymbol() == 'h');
        }
    }

    public void wetAllApplicableTiles() {
        for (Tile tile : tiles.values()) {
            if (tile.isPlanted()) {
                tile.setWatered(true);
            }
        }
        System.out.println("Semua tanaman yang ada telah tersiram oleh hujan.");
    }
}
