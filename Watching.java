import GameCalendar.Model.GameTime;

public class Watching extends Action {

    public Watching() {
        super(5, 15);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if (!player.getLocation_inworld().equalsIgnoreCase("House")) {
            System.out.println("Kamu hanya bisa menonton TV di dalam rumah.");
            return;
        }

        player.setEnergy(player.getEnergy() - energycost);
        gameTime.addTime(timeCostInMinute);

        System.out.println("Kamu menonton TV di rumah. Energi -5, waktu +15 menit.");
    }
}
