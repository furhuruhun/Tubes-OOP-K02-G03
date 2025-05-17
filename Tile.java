public class Tile {
    private char symbol;
    private Seeds plantedSeed;

    public Tile() {
        this.symbol = '.';
        this.plantedSeed = null;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public Seeds getPlantedSeed() {
        return plantedSeed;
    }

    public void setPlantedSeed(Seeds seed) {
        this.plantedSeed = seed;
    }

    public boolean isPlanted() {
        return plantedSeed != null;
    }

    public void clearTile() { //biar habis harvest blik ke default
        this.symbol = '.';
        this.plantedSeed = null;
    }
}
