package Pond;
import Player.Location;

public class Pond{
    private String name;
    private Location location;
    
    public Pond(){
        this.name = "Pond";
    }

    public void greet() {
        System.out.println("Welcome to the " + name + "!");
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
}
