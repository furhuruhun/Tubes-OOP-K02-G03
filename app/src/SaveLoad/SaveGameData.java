package SaveLoad;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class SaveGameData {
    public PlayerData playerData;
    public FarmData farmData;
    public GameTimeData gameTimeData;
    public List<NpcSaveData> npcsData;
    public Map<String, Boolean> recipesUnlocked; // Nama resep -> status unlocked
    public StatisticsData statisticsData;

    public SaveGameData() {
        // Inisialisasi koleksi untuk menghindari NullPointerException saat diakses nanti
        this.npcsData = new ArrayList<>();
        this.recipesUnlocked = new HashMap<>();
    }

    // Konstruktor dengan semua field (opsional, tapi bisa berguna)
    public SaveGameData(PlayerData playerData, FarmData farmData, GameTimeData gameTimeData,
                        List<NpcSaveData> npcsData, Map<String, Boolean> recipesUnlocked,
                        StatisticsData statisticsData) {
        this.playerData = playerData;
        this.farmData = farmData;
        this.gameTimeData = gameTimeData;
        this.npcsData = (npcsData != null) ? npcsData : new ArrayList<>();
        this.recipesUnlocked = (recipesUnlocked != null) ? recipesUnlocked : new HashMap<>();
        this.statisticsData = statisticsData;
    }
}
