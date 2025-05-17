package GameCalendar.Model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageSeason {
    private final Map<Season, List<Crop>> seasonCrops = new HashMap<>();
    private final Map<Season, List<Fish>> seasonFish = new HashMap<>();

    public List<Crop> getCropsThisSeason(Season season) {
        return seasonCrops.getOrDefault(season, Collections.emptyList());
    }

    public List<Fish> getFishThisSeason(Season season) {
        return seasonFish.getOrDefault(season, Collections.emptyList());
    }
}