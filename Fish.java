public class Fish extends NonBuyableItems{
    private String season;
    private int[] time;
    private String weather;
    private String[] location;
    private String fishType;
    private int hargaJual;

    public Fish(String name, String season, int[] time, String weather, String[] location, String fishType, int hargaJual) {
        super(name);
        this.season = season;
        this.time = time;
        this.weather = weather;
        this.location = location;
        this.fishType = fishType;
        this.hargaJual = hargaJual;
    }
    public String getSeason() {
        return season;
    }
    public void setSeason(String season) {
        this.season = season;
    }
    public int[] getTime() {
        return time;
    }
    public void setTime(int[] time) {
        this.time = time;
    }
    public String getWeather() {
        return weather;
    }
    public void setWeather(String weather) {
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
}
