package Action;

import GameCalendar.Model.GameTime;
import Player.NPC;
import Player.Player;

public class Proposing extends Action {
    private NPC npc;

    public Proposing(NPC npc) {
        super(10, 60); // 10 energy jika diterima, waktu tetap 60 menit
        this.npc = npc;
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        int actualEnergyCost;
        if (player.getEnergy() >= -20+this.energyCost) {
            if (npc.getHeartpoints() == 150 && player.haveitem("Proposal Ring")) {
                System.out.println("Yay! " + npc.getName() + " menerima lamaranmu.");
                npc.setRelationshipStatus("fiance");
                actualEnergyCost = this.energyCost; // 10
                npc.setDayEngaged(gametime.getCurrDay()); // set hari ke berapa doi dilamar
            } else {
                System.out.println("Lamaranmu ditolak... " + npc.getName() + " belum siap.");
                npc.setRelationshipStatus("single");
                actualEnergyCost = 20;
            }

            player.setEnergy(player.getEnergy() - actualEnergyCost);
            gametime.addTime(this.timeCostInMinute);
        }
        else {
            System.out.println("Yah, kurang siap nih dalam melamar (energi kureng)");
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