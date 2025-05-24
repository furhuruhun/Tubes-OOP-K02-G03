package Action;

import GameCalendar.Model.GameTime;
import Player.Player;
import farm.FarmMap.FarmMap;
import farm.FarmMap.Tile;
import items.Items;

public class Recover extends Action {
    //asumsi: tile yang bisa di recover itu tile yang dipijak oleh player.
    public Recover() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        if (player.getEnergy() < energyCost) {
            System.out.println("Energi tidak cukup.");
            return;
        }

        FarmMap farmMap = player.getFarmname().getFarmMap();
        Tile tile = farmMap.getTile(player.getLocation_infarm());

        if (!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Harus berada di dalam farm.");
            return;
        }
        
        if (player.haveitem(new Items("Pickaxe")) && player.getFarmname().getFarmMap().isTillable(player.getLocation_infarm())) {
            tile.clearTile();
            player.setEnergy(player.getEnergy() - energyCost);
            gametime.addTime(5);
            System.out.println("Tile (" + tile +  ") berhasil di recover.");
        } else {
            System.out.println("Recover gagal.");
        }
    }
}