import GameCalendar.Model.GameTime;

public class Marry extends Action {
    private NPC fiance;

    public Marry(NPC fiance) {
        super(80, 0); // Energi dikurangi 80, waktu langsung time skip
        this.fiance = fiance;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if (!fiance.getRelationshipStatus().equalsIgnoreCase("fiance")) {
            System.out.println("NPC belum menjadi tunangan.");
            return;
        }

        if (!player.haveitem(new Item("Proposal Ring"))) {
            System.out.println("Kamu tidak memiliki Proposal Ring.");
            return;
        }

        player.setPartner(fiance);
        player.setEnergy(player.getEnergy() - energycost);
        gameTime.timeSkip(22);  // Skip ke pukul 22.00
        player.setLocation_inworld("House");

        System.out.println("Selamat! Kamu menikah dengan " + fiance.getName() + ". Waktu skip ke 22:00.");
    }
}
