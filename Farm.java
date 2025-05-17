public class Farm {
    private String name;
    private Player player;
    private FarmMap farmMap;

    public Farm(String name, Player player) {
        this.name = name;
        this.player = player;
        this.farmMap = new FarmMap(new House(this.player), new Pond(), new ShippingBin());
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
}