package Action;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;


public class ShowLocation extends Action {

    public ShowLocation() {
        super(0, 0);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        clearScreen();
        Location loc = player.getLocation_infarm();
        String world = player.getLocation_inworld();
        System.out.println("Lokasi Player:");
        if (world.equalsIgnoreCase("Farm")) {
            if (loc != null) {
                System.out.println("- Di Farm : (" + loc.getX() + ", " + loc.getY() + ")");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            } else {
                System.out.println("- Di Dunia: " + world + " (koordinat tidak diketahui)");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }
        } else {
            System.out.println("- Di Dunia: " + world);
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
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