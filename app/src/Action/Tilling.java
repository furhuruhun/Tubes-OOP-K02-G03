package Action;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;

public class Tilling extends Action{
    public Tilling() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if(player.haveitem("Hoe")){ //cek punya hoe apa ngga

            if(player.getFarmname().getFarmMap().isEmpty(new Location(player.getLocation_infarm().getX()+1, player.getLocation_infarm().getY()))){ //cek tile di posisi player bisa di tiling apa ngga
                if(player.getEnergy() >= -15){ //cek energi
                    player.getFarmname().getFarmMap().getTile(new Location(player.getLocation_infarm().getX()+1, player.getLocation_infarm().getY())).setSymbol('t'); //set tile di posisi player jadi tilled
                    System.out.println("Kamu sudah menyiapkan tanah");
                    player.setEnergy(player.getEnergy() - 5);
                    gameTime.addTime(5);
                    if (player.getEnergy() <= -20){
                            System.out.println("Kamu sudah tidak punya energi, kamu akan tidur sekarang.");
                            Sleeping sleep = new Sleeping();
                            sleep.perform(player, gameTime);
                            System.out.println("Kamu sudah tidur");
                    }
                } 
                else {
                    System.out.println("Energi kamu tidak cukup untuk tilling");
                }    
            }
            else{
                System.out.println("Gak bisa di tiling");
            }
        }
        else{
            System.out.println("Butuh hoe!");
        }
        try {
            Thread.sleep(500);
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
