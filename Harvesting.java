import src.farmmap.FarmMap;
import src.farmmap.Tile;

class HarvestingAction extends Action {
    private int x, y;

    public HarvestingAction(int x, int y) {
        super(5, 5);
        this.x = x;
        this.y = y;
    }

    @Override
    public void perform(Player player) {
        if (player.getEnergy() < energyCost) {
            System.out.println("Energi tidak cukup.");
            return;
        }
        FarmMap farmMap = player.getFarmname().getFarmMap();
        Tile tile = farmMap.getTile(x, y);

        if (!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Harus berada di dalam farm.");
            return;
        }
        
        if (tile.getSymbol() == 'L') {
            Items crop = new Items("Generic Crop");
            player.addInventory(crop);
            tile.setSymbol('.');
            player.setEnergy(player.getEnergy() - energyCost);
            player.getFarmname().getTime().addMinutes(timeCostInMinute);
            System.out.println("menanam di (" + x + ", " + y + ")");
        } else {
            System.out.println("Harvesting gagal.");
        }
    }
}