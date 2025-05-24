package Action;

import GameCalendar.Model.GameTime;
import Player.NPC;
import Player.Player;
import items.Equipment; 

public class Proposing extends Action {
    private NPC npc;

    public Proposing(NPC npc) {
        super(10, 60); // 10 energy jika diterima, waktu tetap 60 menit
        this.npc = npc;
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        int actualEnergyCost;
        if (player.getEnergy() > this.energyCost) {
            if (npc.getHeartpoints() == 150 && player.haveitem(new Equipment("Proposal Ring"))) {
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
    }
}
