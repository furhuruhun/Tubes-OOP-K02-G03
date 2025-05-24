package Action;

import GameCalendar.Model.GameTime;
import Player.NPC;
import Player.Player;
import items.Items;

public class Gifting extends Action {
    private NPC npc;
    private Items item;

    public Gifting(NPC npc) {
        super(5, 10);
        this.npc = npc;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if(player.getEnergy() > this.energyCost) {
            if (!player.getLocation_inworld().equalsIgnoreCase(npc.getLocation())) {
                System.out.println("Kamu harus berada di rumah NPC untuk memberi hadiah.");
                return;
            }

            if (!player.getInventory().getItemsMap().containsKey(item)) {
                System.out.println("Item tidak tersedia di inventory.");
                return;
            }

            player.getInventory().removeItem(item, 1);
            player.setEnergy(player.getEnergy() - energyCost);
            gameTime.addTime(timeCostInMinute);

            // Penilaian item
            if (item.equals(npc.getLoveditems())) {
                npc.setHeartpoints(npc.getHeartpoints() + 25);
                System.out.println("NPC sangat menyukai hadiahmu! Heartpoint +25");
            } else if (item.equals(npc.getLikeditems())) {
                npc.setHeartpoints(npc.getHeartpoints() + 20);
                System.out.println("NPC menyukai hadiahmu. Heartpoint +20");
            } else if (item.equals(npc.getHateditems())) {
                npc.setHeartpoints(npc.getHeartpoints() - 25);
                System.out.println("NPC membenci hadiahmu... Heartpoint -25");
            } else {
                System.out.println("NPC netral terhadap hadiah ini.");
            }
        }
    }
}