package farm;
import java.time.LocalTime;

import GameCalendar.Model.GameTime;
import GameCalendar.Model.Season;
import GameCalendar.Model.Weather;
import Player.Player;
import farm.FarmMap.FarmMap;

public class Farm {
    private String name;
    private Player player;
    private FarmMap farmMap;
    private LocalTime time;
    private int day;
    private Season season;
    private Weather weather;

    public Farm(String name, Player player, GameTime gameTime, FarmMap farmMap) {
        this.name = name;
        this.player = player;
        this.farmMap = farmMap;
        this.time = gameTime.getCurrTime();
        this.day = gameTime.getCurrDay();
        this.season = gameTime.getCurrSeason();
        this.weather = gameTime.getCurrWeather();
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public FarmMap getFarmMap() {
        return farmMap;
    }

    public void setFarmMap(FarmMap farmMap) {
        this.farmMap = farmMap;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }
}