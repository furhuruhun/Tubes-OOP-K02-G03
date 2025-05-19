import GameCalendar.Model.GameTime;

public class Chatting extends Action {
    private NPC npc;

    public Chatting(NPC npc) {
        super(10, 10);
        this.npc = npc;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if (!player.getLocation_inworld().equalsIgnoreCase(npc.getLocation())) {
            System.out.println("Kamu harus berada di lokasi yang sama dengan NPC untuk mengobrol.");
            return;
        }

        player.setEnergy(player.getEnergy() - energycost);
        npc.setHeartpoints(npc.getHeartpoints() + 10);
        gameTime.addTime(timeCostInMinute);

        System.out.println("Kamu berbicara dengan " + npc.getName() + ". Heartpoint +10, energi -10, waktu +10 menit.");
    }
}
