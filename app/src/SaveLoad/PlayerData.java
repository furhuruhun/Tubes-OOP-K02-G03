package SaveLoad;
import Player.*;
import farm.Farm;
import inventory.Inventory;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    public String name;
    public String gender;
    public int energy;
    public String farmName;
    public String partnerName;
    public float gold;
    public Map<String, Integer> inventory;
    public LocationSaveData locationInFarm;
    public String locationInWorld;
    public int maxEnergy;

    public PlayerData() {
        this.inventory = new HashMap<>();
    }

    public PlayerData(String name, String gender, int energy, String farmName,
                      String partnerName, float gold, Map<String, Integer> inventory,
                      LocationSaveData locationInFarm, String locationInWorld, int maxEnergy) {
        this.name = name;
        this.gender = gender;
        this.energy = energy;
        this.farmName = farmName;
        this.partnerName = partnerName;
        this.gold = gold;
        this.inventory = (inventory != null) ? inventory : new HashMap<>();
        this.locationInFarm = locationInFarm;
        this.locationInWorld = locationInWorld;
        this.maxEnergy = maxEnergy;
    }
}
