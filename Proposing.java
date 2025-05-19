public class Proposing extends Action {
    private NPC npc;

    public Proposing(NPC npc) {
        super(0, 60);
        this.npc = npc;
    }

    @Override
    public void perform(Player player) {
        if (!player.haveitem(new Item("Proposal Ring"))) {
            System.out.println("Kamu butuh Proposal Ring.");
            return;
        }

        if (npc.getHeartPoints() < 150) {
            player.setEnergy(player.getEnergy() - 20);
            System.out.println("Lamaran ditolak huhuuuu.");
        } else {
            npc.setRelationshipStatus("fiance");
            player.setPartner(npc);
            player.setEnergy(player.getEnergy() - 10);
            System.out.println("Selamat! diterima nih sama " + npc.getName());
        }

        player.getFarmname().getTime().addMinutes(timeCostInMinute);
    }
}