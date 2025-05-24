package Action;

import GameCalendar.Model.GameTime;
import Player.Player;
import items.Equipment;

public class Watering extends Action{
    public Watering() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        if(player.getEnergy() > this.energyCost) {    
            if(player.haveitem(new Equipment("Watering Can")) && player.getEnergy() > this.energyCost) {
                System.out.println("Yeay, tanaman segar bugar");
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
}
