import GameCalendar.Model.GameTime;

public class ShowLocation extends Action {

    public ShowLocation() {
        super(0, 0);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        Location loc = player.getLocation_infarm();
        String world = player.getLocation_inworld();
        System.out.println("Lokasi Player:");
        System.out.println("- Di Dunia: " + world);
        System.out.println("- Di Farm : (" + loc.getX() + ", " + loc.getY() + ")");
    }
}
