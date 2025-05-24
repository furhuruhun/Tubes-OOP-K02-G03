package Action;

import GameCalendar.Model.GameTime;
import Player.NPC;
import Player.Player;


public class Marry extends Action {
    private NPC npc;

    public Marry(NPC npc) {
        super(80, 0); //gw bingung cara convert nya ke int
        this.npc=npc;
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        if(player.getEnergy() > this.energyCost) { 
            if(!npc.getRelationshipStatus().equalsIgnoreCase("fiance")) {
                System.out.println("Lah belom tunangan mana bisa nikah sama " + npc.getName());
                return;
            } 
            if(gametime.getCurrDay() <= npc.getDayEngaged()) {
                System.out.println("Nunggu sehari dulu bro, buru-buru amat hoho.");
                return;    
            }
            else {
                System.out.println("Selamat! Kamu menikah dengan " + npc.getName() + "!");
                player.setEnergy(player.getEnergy() - this.energycost);
                gametime.setCurrTime(java.time.LocalTime.of(22, 0));
                npc.setRelationshipStatus("spouse");
                //gimana cara balikin ke rumah nya? 
                player.setLocation_infarm(player.getFarmname().getFarmMap().getHouse().getLocation());
            }
        }
        else {
            System.out.println("Anda belum siap menikah (energi nya kureng)");
        }
    }
}
