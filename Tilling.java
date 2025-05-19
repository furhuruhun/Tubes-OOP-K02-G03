import src.farmmap.FarmMap;
import src.farmmap.Tile;

class TillingAction extends Action {
    private int x, y;

    public TillingAction(int x, int y) {
        super(5, 5);
        this.x = x;
        this.y = y;
    }

    @Override
    public void perform(Player player) {
        if (player.getEnergy() < energyCost) {
            System.out.println("Energi tidak cukup");
            return;
        }
        FarmMap farmMap = player.getFarmname().getFarmMap();
        Tile tile = farmMap.getTile(x, y);

        if (!tile.isTillable()) {
            System.out.println("Tile ini tidak bisa dibajak.");
            return;
        }

        if (!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Harus berada di dalam farm.");
            return;
        }

        if (player.haveitem(new Items("Hoe")) && tile.getSymbol() == '.') {
            tile.setSymbol('t');
            player.setEnergy(player.getEnergy() - energyCost);
            player.getFarmname().getTime().addMinutes(timeCostInMinute);
            System.out.println("Tile di (" + x + ", " + y + ") berhasil di tilling.");
        } else {
            System.out.println("Tilling gagal.");
        }
    }
}