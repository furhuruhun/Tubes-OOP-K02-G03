package Action;

import GameCalendar.Model.GameTime;

public class Moving extends Action {
    private Location destination;

    public Moving(Location destination) {
        super(0, 0); // Tidak konsumsi energi atau waktu
        this.destination = destination;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        player.setLocation_infarm(destination);
        System.out.println("Player telah berpindah ke lokasi baru: (" 
            + destination.getX() + ", " + destination.getY() + ")");
    }
}