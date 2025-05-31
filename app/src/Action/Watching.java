package Action;

import GameCalendar.Model.GameTime;
import Player.Player;

public class Watching extends Action {
    public Watching() {
        super(5, 15);
    }
    
    @Override
    public void perform(Player player, GameTime gametime) {
        if(player.getEnergy() < -15) {
            System.out.println("Kamu tidak punya cukup energi untuk nonton TV!");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        if (player.getFarmname().getFarmMap().isPlayerInHouse(player.getLocation_infarm())) {
            System.out.println("Nyantai dulu sambil nonton TV hihi");
            player.setEnergy(player.getEnergy() - this.energyCost);
            gametime.addTime(this.timeCostInMinute);
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        else {
            System.out.println("Kalo mau nonton ya di dalem rumah dulu dong");
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