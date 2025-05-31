package Action;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
import farm.FarmMap.FarmMap;
import farm.FarmMap.Tile;

public class Recover extends Action {
    //asumsi: tile yang bisa di recover itu tile yang dipijak oleh player.
    public Recover() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        if (player.getEnergy() < -20 + energyCost) {
            System.out.println("Energi tidak cukup.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        FarmMap farmMap = player.getFarmname().getFarmMap();
        Tile tile = farmMap.getTile(new Location(player.getLocation_infarm().getX()+1, player.getLocation_infarm().getY()));

        if (!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Harus berada di dalam farm.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        if (player.haveitem("Pickaxe") && player.getFarmname().getFarmMap().isTillable(new Location(player.getLocation_infarm().getX()+1, player.getLocation_infarm().getY()))) {
            tile.clearTile();
            player.setEnergy(player.getEnergy() - energyCost);
            gametime.addTime(5);
            System.out.println("Tile (" + (player.getLocation_infarm().getX()+1) + ", " + (player.getLocation_infarm().getY()) +  ") berhasil di recover.");
        } else {
            System.out.println("Recover gagal.");
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                for (int i = 0; i < 50; ++i) System.out.println();
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}