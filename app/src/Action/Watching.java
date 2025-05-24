package Action;

import GameCalendar.Model.GameTime;
import Player.Player;

public class Watching extends Action {
    public Watching() {
        super(5, 15);
    }
    
    @Override
    public void perform(Player player, GameTime gametime) {
        if (player.getFarmname().getFarmMap().isPlayerInHouse(player.getLocation_infarm())) {
            System.out.println("Nyantai dulu sambil nonton TV hihi");
            player.setEnergy(player.getEnergy() - this.energyCost);
            gametime.addTime(this.timeCostInMinute);
        }
        else {
            System.out.println("Kalo mau nonton ya di dalem rumah dulu dong");
        }
    }

}
