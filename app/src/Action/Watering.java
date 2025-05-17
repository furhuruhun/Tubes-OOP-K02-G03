package Action;

import Player.Player;

public class Watering extends Action{
    public watering() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, Gametime gametime) {
        if(player.haveitem(new Equipment("Watering Can"))) {
            System.out.println("Yeay, tanaman segar bugar");
            player.setEnergy(player.getEnergy() - this.energycost);
            gametime.addMinutes(this.timeCostInMinute);
        }
        else {
            System.out.println("Anda tidak punya Tempat buat nyiram!!! HUUUUUUU");
        }
    }
}
