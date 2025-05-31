package SaveLoad;

public class GameTimeData {
    public int hour;
    public int minute;
    public int currentDay;
    public String currentSeason;
    public String currentWeather;
    public int rainyDaysThisSeason;
    public boolean isPaused;

    public GameTimeData() {}

    public GameTimeData(int hour, int minute, int currentDay, String currentSeason,
                        String currentWeather, int rainyDaysThisSeason, boolean isPaused) {
        this.hour = hour;
        this.minute = minute;
        this.currentDay = currentDay;
        this.currentSeason = currentSeason;
        this.currentWeather = currentWeather;
        this.rainyDaysThisSeason = rainyDaysThisSeason;
        this.isPaused = isPaused;
    }
}