package src.farmmap;

public class FarmMap {
    private static final int width = 32;
    private static final int height = 32;
    private House house;
    private Pond pond;
    private ShippingBin shippingBin;
    private Tile[][] tiles;

    public FarmMap(House house, Pond pond, ShippingBin) {
        this.house = house;
        this.pond = pond;
        this.shippingBin = shippingBin;
        tiles = new Tile[height][weight];

    }

    public boolean canPlaceStructure(int x, int y, int w, int h) {
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (tiles[y + i][x + j].getSymbol() != '.') {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void generateMap() {
        // membuat peta kosongannya
        for (int i=0; i < height; i++) {
            for(int j=0; j < width; j++) {
                tiles[i][j] = new Tile();
            }
        }
    };

    public void placeRandomHouse() {
        // meletakkan objek rumah secara random di dalam sebuah map
        int houseWidth = 6;
        int houseHeight = 6;
        int attempts = 100; // batas percobaan random

        for(int attempt = 0; attempt < attempts; attempts++) {
            int x = (int)(Math.random() * (width - houseWidth));
            int y = (int)(Math.random() * (height - houseHeight));
            
            if (canPlaceStructure(x, y, houseWidth, houseHeight)) {
                for (int i = 0; i < houseHeight; i++) {
                    for (int j = 0; j < houseWidth; j++) {
                        tiles[y + i][x + j].setSymbol('h');
                    }
                }
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
                for (int i = 0; i < pondWidth; i++) {
                    for (int j = 0; j < pondHeight; j++) {
                        tiles[y + i][x + j].setSymbol('p');
                    }
                }
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
                for (int i = 0; i < shipWidth; i++) {
                    for (int j = 0; j < shipHeight; j++) {
                        tiles[y + i][x + j].setSymbol('s');
                    }
                }
                return; 
            }
        }
    };

    public void placePlayer(Location loc) {
        // Meletakkan objek player sesuai dengan parameter masukan 
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
                System.out.print(tiles[i][j].getSymbol() + " ");
            }
            System.out.println(); // newline setiap baris
        }

    };

    public boolean isSameTile(Location loc) {
        // mengecek apakah di satu lokasi tertentu terdapat dua objek sama (gw lupa ini buat method apa)
    };

    public boolean isEdgeTile(Location loc) {
        // Mengecek apakah player sudah berada di pojok tile/map
    };

    public boolean isEmpty(Location loc) {
        // Mengecek apakah di titik tersebut kosong dari 3 objek atau tidak 
    };

    public boolean isTillable(Location loc) {
        // cek apakah tanah siap tanam
    };

    public boolean isPlanted(Location loc) {
        // cek apakah tanah sudah ditanam sesuatu atau belum
    };
}
