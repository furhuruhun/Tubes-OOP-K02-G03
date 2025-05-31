package SaveLoad;


public class TileSaveData {
    public int x;
    public int y;
    public char symbol;
    public SeedSaveData plantedSeed;
    public boolean isWatered;

    public TileSaveData() {}

    public TileSaveData(int x, int y, char symbol, SeedSaveData plantedSeed, boolean isWatered) {
        this.x = x;
        this.y = y;
        this.symbol = symbol;
        this.plantedSeed = plantedSeed;
        this.isWatered = isWatered;
    }
}

