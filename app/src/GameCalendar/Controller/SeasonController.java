package GameCalendar.Controller;

import java.util.Iterator;
import java.util.List;

import GameCalendar.Model.Season;
import GameCalendar.Model.ManageSeason;
import items.Fish;
// import GameCalendar.Model.Crop;
// import GameCalendar.Model.Fish;
import items.Seeds;

public class SeasonController {
    private final ManageSeason manageSeason;

    public SeasonController(ManageSeason manageSeason) {
        this.manageSeason = manageSeason;
    }

    // Menjaga crop yang kompatibel dengan musim
    public void checkCropSurvival(List<Seeds> allCrops, Season currSeason) {
        Iterator<Seeds> iterator = allCrops.iterator();
        while (iterator.hasNext()) {
            Seeds crop = iterator.next();
            if (!crop.isCompatibleWith(currSeason)) {
                iterator.remove(); // Menghapus elemen saat iterasi
            }
        }
    }

    // Validasi ketersediaan ikan sesuai musim
    public boolean validateFishAvailability(Fish fish, Season currSeason) {
        List<Fish> fishInSeason = manageSeason.getFishThisSeason(currSeason);
        return fishInSeason != null && fishInSeason.contains(fish);
    }
}
