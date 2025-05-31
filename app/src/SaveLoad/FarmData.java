package SaveLoad;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class FarmData {
    public String farmName;
    public LocationSaveData houseLocation;
    public LocationSaveData pondLocation;
    public LocationSaveData shippingBinLocation;
    public List<TileSaveData> tiles;
    public Map<String, Integer> shippingBinContents;

    public FarmData() {
        this.tiles = new ArrayList<>();
        this.shippingBinContents = new HashMap<>();
    }

    public FarmData(String farmName, LocationSaveData houseLocation, LocationSaveData pondLocation,
                    LocationSaveData shippingBinLocation, List<TileSaveData> tiles,
                    Map<String, Integer> shippingBinContents) {
        this.farmName = farmName;
        this.houseLocation = houseLocation;
        this.pondLocation = pondLocation;
        this.shippingBinLocation = shippingBinLocation;
        this.tiles = (tiles != null) ? tiles : new ArrayList<>();
        this.shippingBinContents = (shippingBinContents != null) ? shippingBinContents : new HashMap<>();
    }
}
