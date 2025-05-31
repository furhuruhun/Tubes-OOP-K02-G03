package items;

import GameCalendar.Model.Season;
import GameCalendar.Model.Weather;

public class Fish extends NonBuyableItems{
    private Season[] season;
    private int x;
    private int y;
    private int[] time; // Fishing available from 6 AM to 6 PM
    private Weather[] weather;
    private String[] location;
    private String fishType;
    private int hargaJual;

    public Fish(String name, Season[] season, Weather[] weather, String[] location, String fishType, int hargaJual, int x, int y) {
        super(name);
        this.season = season;
        this.weather = weather;
        this.location = location;
        this.fishType = fishType;
        this.hargaJual = hargaJual;
        this.x = x;
        this.y = y;
        this.time = new int[]{x, y};
    }
    public Season[] getSeason() {
        return season;
    }
    public void setSeason(Season[] season) {
        this.season = season;
    }
    public int[] getTime() {
        return time;
    }
    public void setTime(int[] time) {
        this.time = time;
    }
    public Weather[] getWeather() {
        return weather;
    }
    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }
    public String[] getLocation() {
        return location;
    }
    public void setLocation(String[] location) {
        this.location = location;
    }
    public String getFishType() {
        return fishType;
    }
    public void setFishType(String fishType) {
        this.fishType = fishType;
    }
    public int getHargaJual() {
        return hargaJual;
    }
    public void setHargaJual(int hargaJual) {
        this.hargaJual = hargaJual;
    }
    public boolean isfishingable(){
        if (this.time == null || this.time.length < 2) { // Pengecekan keamanan
            return false;
        }
        int currentHour = java.time.LocalTime.now().getHour(); // Hati-hati: ini waktu sistem, bukan waktu game
        // Logika ini juga tidak menangani kasus waktu lintas tengah malam dengan benar.
        return (currentHour >= this.time[0] && currentHour <= this.time[1]);
    }
}
