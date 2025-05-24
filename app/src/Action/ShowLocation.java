package Action;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;


public class ShowLocation extends Action {

    public ShowLocation() {
        super(0, 0);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        Location loc = player.getLocation_infarm();
        String world = player.getLocation_inworld();
        System.out.println("Lokasi Player:");
        if(loc == null) {
            System.out.println("- Di Dunia: " + world);
        } else {
        System.out.println("- Di Farm : (" + loc.getX() + ", " + loc.getY() + ")");
        }
    }
}