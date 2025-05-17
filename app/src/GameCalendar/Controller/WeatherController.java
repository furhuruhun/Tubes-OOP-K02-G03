package GameCalendar.Controller;

import GameCalendar.Model.GameTime;
import GameCalendar.Model.Weather;
import GameCalendar.Model.ManageWeather;
// import GameCalendar.Model.FarmMap; 

public class WeatherController {
    private static final double RAINY_PROBABILITY = 0.5;  // Probabilitas cuaca hujan
    private final GameTime gameTime;
    private final ManageWeather manage;

    public WeatherController(GameTime gameTime, ManageWeather manage) {
        this.gameTime = gameTime;
        this.manage = manage;
    }

    // Menghasilkan cuaca acak (SUNNY atau RAINY)
    public void generateRandomWeather() {
        Weather w = Math.random() < RAINY_PROBABILITY ? Weather.SUNNY : Weather.RAINY;
        gameTime.setCurrWeather(w);
        
        // Jika cuaca hujan, tambahkan satu hari hujan
        if (w == Weather.RAINY) {
            manage.incrementRainyDays();
        }
    }

    // Menerapkan efek hujan pada farm jika cuaca hujan
    public void applyRainEffect(FarmMap map) {
        if (gameTime.getCurrWeather() == Weather.RAINY) {
            map.wetAllApplicableTiles();  // Menandai semua tile yang relevan sebagai basah
        }
    }
}
