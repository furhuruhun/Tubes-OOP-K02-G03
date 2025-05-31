package House;

import Player.Location; // Assuming Location is in Player package

public class House {
    private String name;
    private Location location; // Top-left location of the house on the FarmMap

    // Dimensions of the house as per PDF (6x6)
    public static final int DEFAULT_WIDTH = 6;
    public static final int DEFAULT_HEIGHT = 6;

    public House() {
        this.name = "Player's House";
        // Location is set by FarmMap when placing the house
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // Optional: getters for dynamic dimensions if you ever need them,
    // but for a fixed size, the static final fields are usually sufficient.
    public int getWidth() {
        return DEFAULT_WIDTH;
    }

    public int getHeight() {
        return DEFAULT_HEIGHT;
    }
}