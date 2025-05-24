package Action;

import Map.Tile;
import Player.Player;

public class Sleeping extends Action{
    public Sleeping() {
        super(0,0);
    }

    @Override
    public void perform(Player player) {
    String location = player.getLocation_inworld();
    Location pos = player.getLocation_infarm();
    Tile tile = player.getFarmname().getFarmMap().getTile(pos.getX(), pos.getY());

    if (!location.equals("Farm") || tile.getSymbol() != 'H') {
        System.out.println("Kamu harus berada di dalam rumah (simbol 'H') untuk tidur.");
        return;
    }

    int energy = player.getEnergy();
    if (energy == 0) {
        player.setEnergy(10);
    } else if (energy < (player.getMaxEnergy() / 10)) {
        player.setEnergy(player.getMaxEnergy() / 2);
    } else {
        player.setEnergy(player.getMaxEnergy());
    }
    player.getFarmname().nextDay();
    System.out.println("Selamat tidur. Energi telah diisi ulang.");      
    }
}