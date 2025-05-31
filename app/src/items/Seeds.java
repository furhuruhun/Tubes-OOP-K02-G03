package items;

import GameCalendar.Model.GameTime;
import GameCalendar.Model.Season;


public class Seeds extends BuyableItems{
    private Season season;
    private int startday;
    private int daystoharvest;
    private Crops crops;


    public Seeds(String name, Season season, int daystoharvest, Crops crops, int hargaJual, int hargaBeli, GameTime gameTime) {
        super(name, hargaJual, hargaBeli);
        this.season = season;
        this.daystoharvest = daystoharvest;
        this.startday = gameTime.getCurrDay();
        this.crops = crops;
    }
    public Season getSeason() {
        return season;
    }
    public void setSeason(Season season) {
        this.season = season;
    }
    public int getDaystoharvest() {
        return daystoharvest;
    }
    public void setDaystoharvest(int daystoharvest) {
        this.daystoharvest = daystoharvest;
    }
    public int getStartday() {
        return startday;
    }
    public void setStartday(int startday) {
        this.startday = startday;
    }
    public Crops getCrops() {
        return crops;
    }
    public void setCrops(Crops crops) {
        this.crops = crops;
    }
    public boolean isHarvestable(GameTime gameTime) {
        int daysPassed = gameTime.getCurrDay() - startday;
        if (daysPassed >= daystoharvest) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCompatibleWith(Season currSeason) {
        return this.season == currSeason;
    }

}
