package Action;

import GameCalendar.Model.GameTime;
import Player.NPC;
import Player.Player;


public class Chatting extends Action {
    private NPC npc;

    public Chatting(NPC npc) {
        super(10, 10);
        this.npc = npc;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if(player.getEnergy() > -20 + this.energyCost) {
            if (!player.getLocation_inworld().equalsIgnoreCase(npc.getLocation())) {
                System.out.println("Kamu harus berada di lokasi yang sama dengan NPC untuk mengobrol (rumah NPC).");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;
            }

            player.setEnergy(player.getEnergy() - energyCost);
            npc.setHeartpoints(npc.getHeartpoints() + 10);
            gameTime.addTime(timeCostInMinute);
            System.out.println("Kamu berbicara dengan " + npc.getName());
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        else{
            System.out.println("Duh, Social battery ku habis, ngobrolnya next time aja deh");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
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

