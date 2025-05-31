package Player;

import farm.Farm;
import inventory.Inventory;
import items.Items;

public class Player {
    private String name;
    private String gender;
    private int energy;
    private Farm farmname;
    private NPC partner;
    private float gold;
    private Inventory inventory;
    private Location location_infarm;
    private String location_inworld;
    private int maxenergy = 100;

    public Player(String name, String gender, Farm farmname, NPC partner, Inventory inventory) {
        this.name = name;
        this.gender = gender;
        this.farmname = farmname;
        this.partner = null;
        this.energy = maxenergy;
        this.gold = 0;
        this.inventory = inventory;
        this.location_infarm = new Location(0, 0);
        this.location_inworld = "Farm";
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender){
        if(gender.equals("Laki-laki") || gender.equals("Perempunan")){
            this.gender = gender;
        }
        else{
            System.out.println("Masukkan Laki-laki atau Perempuan");
        }
    }
    public int getEnergy(){
        return energy;
    }
    public void setEnergy(int energy){
        if(energy > maxenergy){
            this.energy = maxenergy;
        }
        else if(energy < -20){
            this.energy = -20;
        }
        else{
            this.energy = energy;
        }
    }
    public Farm getFarmname() {
        return farmname;
    }
    public void setFarmname(Farm farmname) {
        this.farmname = farmname;
    }
    public NPC getPartner() {
        if(partner == null) {
            return null;
        } 
        else {
            return partner;
        }
    }
    public void setPartner(NPC partner) {
        this.partner = partner;
    }
    public float getGold() {
        return gold;
    }
    public void setGold(float gold) {
        if(gold < 0) {
            System.out.println("Gold cannot be negative.");
        } else {
            this.gold = gold;
        }
    }
    public Inventory getInventory() {
        return inventory;
    }
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public void addInventory(Items item, int qty) {
        inventory.addItem(item, qty);
    }
    public void removefromInventory(Items item, int quantity) {
        if(inventory.getItemsMap().containsKey(item)) {
            inventory.removeItem(item, quantity);
        } else {
            System.out.println("Item not found in inventory.");
        }
    }
    public Location getLocation_infarm() {
        return location_infarm;
    }
    public void setLocation_infarm(Location location_infarm) {
        this.location_infarm = location_infarm;
    }
    public String getLocation_inworld() {
        return location_inworld;
    }
    public void setLocation_inworld(String location_inworld) {
        this.location_inworld = location_inworld;
    }
    public boolean haveitem(String item) {
        return inventory.getItemsMap().keySet().stream()
                .anyMatch(i -> i.getName().equalsIgnoreCase(item));
    }
    public void showStatus() {
        System.out.println("Player Name: " + name);
        System.out.println("Gender: " + gender);
        System.out.println("Energy: " + energy);
        System.out.println("Gold: " + gold);
        System.out.println("Location in Farm: " + location_infarm);
        System.out.println("Location in World: " + location_inworld);
        System.out.println("Inventory: \n" + inventory.printInventory());
        if (partner != null) {
            System.out.println("Partner: " + partner.getName());
        } else {
            System.out.println("No partner yet.");
        }
    }
}
