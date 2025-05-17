
import java.util.Iterator;
import java.util.List;

// import GameCalendar.Model.Crop;
// import GameCalendar.Model.Fish;

public class SeasonController {
    private final ManageSeason manageSeason;

    public SeasonController(ManageSeason manageSeason) {
        this.manageSeason = manageSeason;
    }

    // Menjaga crop yang kompatibel dengan musim
    public void checkCropSurvival(List<Crops> allCrops, Season currSeason) {
        Iterator<Crops> iterator = allCrops.iterator();
        while (iterator.hasNext()) {
            Crops crop = iterator.next();
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
