public class Farm {
    private String name;
    private Player player;
    private FarmMap farmMap;
    private Time time;
    private int day;
    private Season season;
    private Weather weather;

    public Farm(String name, Player player) {
        this.name = name;
        this.player = player;
        this.farmMap = new FarmMap();
        this.time = new Time();
        this.day = 1;
        this.season = new Season(); 
        this.weather = new Weather();
    }

    public String getName() {
        return name;
    }
    
    public void setName() {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer() {
        this.player = player;
    }

    public FarmMap getFarmMap() {
        return farmMap;
    }

    public void setFarmMap() {
        this.farmMap = farmMap;
    }

    public Time getTime() {
        return time;
    }

    public void setTime() {
        this.time = time;
    }

    public int getDay() {
        return day;
    }

    public void setDay() {
        this.day = day;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason() {
        this.season = season;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather() {
        this.weather = weather;
    }

    public void nextDay() {
        day++;
        time.setTime(6, 0);
        weather.setWeather("Sunny");
    }
}
