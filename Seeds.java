public class Seeds extends BuyableItems{
    private Season season;
    private int daystoharvest;

    public Seeds(String name, Season season, int daystoharvest, int hargaJual, int hargaBeli) {
        super(name, hargaJual, hargaBeli);
        this.season = season;
        this.daystoharvest = daystoharvest;
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

}
