package Action;

import GameCalendar.Model.GameTime; 
import Player.NPC;
import Player.Player;
import Player.Location;


public class Marry extends Action {
    private NPC npc;

    public Marry(NPC npc) {
        super(80, 0);
        this.npc=npc;
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        if(player.getEnergy() >= -20 + this.energyCost) { 
            if(!npc.getRelationshipStatus().equalsIgnoreCase("fiance")) {
                System.out.println("Lah belom tunangan mana bisa nikah sama " + npc.getName());
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;
            }
            if (gametime.getCurrTime().getHour() >= 22) {
                System.out.println("Kamu tidak bisa menikah sekarang, sudah terlalu malam. Silakan coba lagi besok.");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;
            }
            if(gametime.getCurrDay() <= npc.getDayEngaged()) {
                System.out.println("Nunggu sehari dulu bro, buru-buru amat hoho.");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;    
            }
            else {
                System.out.println("Selamat! Kamu menikah dengan " + npc.getName() + "!");
                player.setEnergy(player.getEnergy() - this.energyCost);
                gametime.setCurrTime(java.time.LocalTime.of(22, 0));
                npc.setRelationshipStatus("spouse");
                player.setPartner(this.npc);
                //gimana cara balikin ke rumah nya? 
                player.setLocation_inworld("Farm");
                player.setLocation_infarm(new Location(player.getFarmname().getFarmMap().getHouse().getLocation().getX() + 3, 
                                                       player.getFarmname().getFarmMap().getHouse().getLocation().getY() + 6));
                player.getFarmname().getFarmMap().getTile(player.getLocation_infarm()).setSymbol('p');
            }
            if (player.getEnergy() <= -20){
                System.out.println("Kamu sudah tidak punya energi, kamu akan tidur sekarang.");
                Sleeping sleep = new Sleeping();
                sleep.perform(player, gametime);
                System.out.println("Kamu sudah tidur");
            }
        }
        else {
            System.out.println("Anda belum siap menikah (energi nya kureng)");
        }
        try {
            Thread.sleep(1500);
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