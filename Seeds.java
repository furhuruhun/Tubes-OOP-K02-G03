public class Seeds extends BuyableItems{
    private String season;
    private int daystoharvest;

    public Seeds(String name, String season, int daystoharvest, int hargaJual, int hargaBeli) {
        super(name, hargaJual, hargaBeli);
        this.season = season;
        this.daystoharvest = daystoharvest;
    }
    public String getSeason() {
        return season;
    }
    public void setSeason(String season) {
        this.season = season;
    }
    public int getDaystoharvest() {
        return daystoharvest;
    }
    public void setDaystoharvest(int daystoharvest) {
        this.daystoharvest = daystoharvest;
    }

}
