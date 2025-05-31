package Action;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
public class Visiting extends Action {
    private String destination;
    private Location temp;

    public Visiting(String destination, Location temp) {
        super(10, 15);
        this.destination = destination;
        this.temp = temp;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if (player.getEnergy() < -20 + energyCost) {
            System.out.println("Kamu tidak punya cukup energi untuk mengunjungi " + destination + ".");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        if(player.getLocation_inworld().equals("Farm")) {
            player.getFarmname().getFarmMap().getTile(player.getLocation_infarm()).setSymbol('.');
            temp = player.getLocation_infarm();
        }
        if (destination.equalsIgnoreCase("Farm")) {
            player.setLocation_inworld("Farm");
            player.setLocation_infarm(temp);
            player.getFarmname().getFarmMap().getTile(temp).setSymbol('p');
            System.out.println("Kamu kembali ke Farm.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        player.setLocation_inworld(destination);
        player.setEnergy(player.getEnergy() - energyCost);
        gameTime.addTime(timeCostInMinute);

        System.out.println("Kamu mengunjungi " + destination + ". Energi -10, waktu +15 menit.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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