import GameCalendar.Model.GameTime;

public class Visiting extends Action {
    private String destination;

    public Visiting(String destination) {
        super(10, 15);
        this.destination = destination;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        player.setLocation_inworld(destination);
        player.setEnergy(player.getEnergy() - energycost);
        gameTime.addTime(timeCostInMinute);

        System.out.println("Kamu mengunjungi " + destination + ". Energi -10, waktu +15 menit.");
    }
}
