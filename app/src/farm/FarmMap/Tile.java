package farm.FarmMap;

import items.Seeds;

public class Tile {
    private char symbol;
    private Seeds plantedSeed;
    private boolean isWatered;

    public Tile() {
        this.symbol = '.';
        this.plantedSeed = null;
        this.isWatered = false;
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
        this.isWatered = false;
    }
    public boolean isWatered() {
        return isWatered;
    }

    public void setWatered(boolean watered) {
        isWatered = watered;
    }

    

}
