package Action;

import src.farmmap.FarmMap;
import src.farmmap.Tile;

class PlantingAction extends Action {
    private int x, y;
    private Items seed;

    public PlantingAction(int x, int y, Items seed) {
        super(5, 5);
        this.x = x;
        this.y = y;
        this.seed = seed;
    }

    @Override
    public void perform(Player player) {
        if (player.getEnergy() < energyCost) {
            System.out.println("Energi tidak cukup");
            return;
        }
        FarmMap farmMap = player.getFarmname().getFarmMap();
        Tile tile = farmMap.getTile(x, y);

        if (!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Harus berada di dalam farm.");
            return;
        }
        
        if (player.haveitem(seed) && tile.getSymbol() == 't') {
            player.removefromInventory(seed, 1);
            tile.setSymbol('l');
            player.setEnergy(player.getEnergy() - energyCost);
            player.getFarmname().getTime().addMinutes(timeCostInMinute);
            System.out.println("Menanam " + seed.getName() + " di (" + x + ", " + y + ")");
        } else {
            System.out.println("Planting gagal.");
        }
    }
}