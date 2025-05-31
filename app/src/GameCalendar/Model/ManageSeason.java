package GameCalendar.Model;

import items.Crops;
import items.Fish;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ManageSeason {
    private final Map<Season, List<Crops>> seasonCrops = new HashMap<>();
    private final Map<Season, List<Fish>> seasonFish = new HashMap<>();

    public List<Crops> getCropsThisSeason(Season season) {
        return seasonCrops.getOrDefault(season, Collections.emptyList());
    }

    public List<Fish> getFishThisSeason(Season season) {
        return seasonFish.getOrDefault(season, Collections.emptyList());
    }
}