package farm.FarmMap;

import java.util.HashMap;
import java.util.Map;

import House.House;
import Player.Location;
import Player.Player;
import Pond.Pond;
import ShippingBin.ShippingBin;

public class FarmMap {
    private static final int width = 32;
    private static final int height = 32;
    private House house;
    private Pond pond;
    private ShippingBin shippingBin;
    private Map<Location, Tile> tiles;

    public FarmMap(House house, Pond pond, ShippingBin ShippingBin) {
        this.house = house;
        this.pond = pond;
        this.shippingBin = shippingBin;
        tiles = new HashMap<>();
    }

    public Tile getTile(Location loc) {
        return tiles.get(loc);
    }


    public boolean canPlaceStructure(int x, int y, int w, int h) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                Location loc = new Location(x + j, y + i);
                Tile tile = tiles.get(loc);
                if (tile == null || tile.getSymbol() != '.') {
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
                tiles.get(loc).setSymbol(symbol);
            }
        }
    }  

    public void generateMap() {
        // membuat peta kosongannya
        for (int i=0; i < height; i++) {
            for(int j=0; j < width; j++) {
                Location loc = new Location(j, i);
                tiles.put(loc, new Tile());
            }
        }
    }

    public void placeRandomHouse() {
        // meletakkan objek rumah secara random di dalam sebuah map
        int houseWidth = 6;
        int houseHeight = 6;
        int attempts = 100; // batas percobaan random

        for(int attempt = 0; attempt < attempts; attempts++) {
            int x = (int)(Math.random() * (width - houseWidth));
            int y = (int)(Math.random() * (height - houseHeight));
            
            if (canPlaceStructure(x, y, houseWidth, houseHeight)) {
                placeStructure(x, y, houseWidth, houseHeight, 'h');
                return;
            }
        }
    };

    public void placeRandomPond() {
        // meletakkan objek kolam secara random di dalam sebuah map
        int pondWidth = 3, pondHeight = 2, attempts = 100; // batas percobaan random

        for(int attempt = 0; attempt < attempts; attempts++) {
            int x = (int)(Math.random() * (width - pondWidth));
            int y = (int)(Math.random() * (height - pondHeight));
            
            if (canPlaceStructure(x, y, pondWidth, pondHeight)) {
                placeStructure(x, y, pondWidth, pondHeight, 'p');
                return;
            }
        }

    };

    public void placeRandomShippingBin() {
        // meletakkan objek ShippingBin secara random di dalam sebuah map

        int shipWidth = 3, shipHeight = 2, attempts = 100; // batas percobaan random

        for(int attempt = 0; attempt < attempts; attempts++) {
            int x = (int)(Math.random() * (width - shipWidth));
            int y = (int)(Math.random() * (height - shipHeight));
            
            if (canPlaceStructure(x, y, shipWidth, shipHeight)) {
                placeStructure(x, y, shipWidth, shipHeight, 's');
                return;
            }
        }
    };

    public void placePlayer(Location loc) {
        // Meletakkan objek player sesuai dengan parameter masukan 
        if (tiles.containsKey(loc) && tiles.get(loc).getSymbol() == '.') {
            tiles.get(loc).setSymbol('P');
        } else {
            System.out.println("Tidak bisa menempatkan player di lokasi tersebut.");
        }
    };

    public void printMap() {
        // Menjalankan perintah perintah sebelumnya
        generateMap();           // Buat map kosong (hanya '.')
        placeRandomHouse();      // Tempatkan rumah (h)
        placeRandomPond();       // Tempatkan kolam (p)
        placeRandomShippingBin(); // Tempatkan kotak kirim (s)

        // Cetak hasil map
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Location loc = new Location(j, i);
                Tile tile = tiles.get(loc);
                System.out.print(tile != null ? tile.getSymbol() + " " : "? ");            
            }
        System.out.println(); // newline setiap baris
        }
    };

    // public boolean isSameTile(Location loc) {
    //     // mengecek apakah di satu lokasi tertentu terdapat dua objek sama (gw lupa ini buat method apa)
    // };

    public boolean isEdgeTile(Location loc,Player player) {
        // Mengecek apakah player sudah berada di pojok tile/map
            return ((player.getLocation_infarm().getX() == 31 && player.getLocation_infarm().getY() == 31) || (player.getLocation_infarm().getX() == 31 && player.getLocation_infarm().getY() == 0) || (player.getLocation_infarm().getX() == 0 && player.getLocation_infarm().getY() == 0) || (player.getLocation_infarm().getX() == 0 && player.getLocation_infarm().getY() == 31));
    };

    public boolean isEmpty(Location loc) {
        // Mengecek apakah di titik tersebut kosong dari 3 objek atau tidak 
        Tile tile = tiles.get(loc);
        return tile != null && tile.getSymbol() == '.';
    };

    public boolean isTillable(Location loc) {
        // cek apakah tanah siap tanam
        Tile tile = tiles.get(loc);
        return tile != null && tile.getSymbol() == 't';
    };

    public boolean isPlanted(Location loc) {
        // cek apakah tanah sudah ditanam sesuatu atau belum
        Tile tile = tiles.get(loc);
        return tile != null && tile.isPlanted();
    };

    public boolean isPlayerInHouse(Location playerLoc) {
    Location houseLoc = house.getLocation(); 
    int houseWidth = 6;
    int houseHeight = 6;

    int houseX = houseLoc.getX(); // x pojok kiri atas rumah
    int houseY = houseLoc.getY(); // y pojok kiri atas rumah

    return (playerLoc.getX() >= houseX && playerLoc.getX() < houseX + houseWidth) &&
           (playerLoc.getY() >= houseY && playerLoc.getY() < houseY + houseHeight);
    };

}
