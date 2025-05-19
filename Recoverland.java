import src.farmmap.FarmMap;
import src.farmmap.Tile;

class RecoverLandAction extends Action {
    private int x, y;

    public RecoverLandAction(int x, int y) {
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
        
        if (player.haveitem(new Items("Pickaxe")) && tile.getSymbol() == 't') {
            tile.setSymbol('.');
            player.setEnergy(player.getEnergy() - energyCost);
            player.getFarmname().getTime().addMinutes(timeCostInMinute);
            System.out.println("Tile di (" + x + ", " + y + ") berhasil di recover.");
        } else {
            System.out.println("Recover gagal.");
        }
    }
}