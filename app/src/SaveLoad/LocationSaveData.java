package SaveLoad;

public class LocationSaveData {
    public int x;
    public int y;

    public LocationSaveData() {}

    public LocationSaveData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public LocationSaveData(Player.Location playerLocation) {
        if (playerLocation != null) {
            this.x = playerLocation.getX(); //
            this.y = playerLocation.getY(); //
        }
    }

    public Player.Location toPlayerLocation() {
        return new Player.Location(this.x, this.y); //
    }
}