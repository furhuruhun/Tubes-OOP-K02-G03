package Action;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;

public class Watering extends Action{
    public Watering() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        if (player.getFarmname().getFarmMap().isPlanted(new Location(player.getLocation_infarm().getX()+1, player.getLocation_infarm().getY()))) {
            if(player.getEnergy() >= -15) {
                if(player.haveitem("Watering Can") && player.getEnergy() > this.energyCost) {
                    System.out.println("Yeay, tanaman segar bugar");
                    player.getFarmname().getFarmMap().getTile(new Location(player.getLocation_infarm().getX()+1, player.getLocation_infarm().getY())).setWatered(true);
                    player.setEnergy(player.getEnergy() - this.energyCost);
                    gametime.addTime(this.timeCostInMinute);
                }
                else {
                    System.out.println("Mau nyiram pake apa kamu!!! HUUUUUUU");
                }
            } else {
                System.err.println("Yah belum bisa nyiram nih, (kurang energi)");
            }
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