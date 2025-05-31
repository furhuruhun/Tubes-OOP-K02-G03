package Action;

import GameCalendar.Model.GameTime;
import Player.NPC;
import Player.Player;
import items.Items;

public class Gifting extends Action {
    private NPC npc;
    private Items itemToGift;

    public Gifting(NPC npc, Items itemToGift) {
        super(5, 10);
        this.npc = npc;
        this.itemToGift = itemToGift;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if(player.getEnergy() > -20 + this.energyCost) {
            if (!player.getLocation_inworld().equalsIgnoreCase(npc.getLocation())) {
                System.out.println("Kamu harus berada di lokasi yang sama dengan NPC untuk surprise-in orang lain.");
                System.out.println("Coba samper ke rumah NPC (" + npc.getLocation() + ") untuk memberi hadiah kepada " + npc.getName() + ".");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return;
            }

            if (this.itemToGift == null) {
                System.out.println("Tidak ada item yang dipilih untuk diberikan.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return;
            }

            if (!player.getInventory().getItemsMap().containsKey(itemToGift)) {
                System.out.println("Item '" + itemToGift.getName() + "' tidak tersedia di inventory.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return;
            }

            if( player.getInventory().getItemsMap().get(itemToGift) <= 0) {
                System.out.println("Item '" + itemToGift.getName() + "' jumlahnya habis, coba kasih barang yang lain.");
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            }

            player.getInventory().removeItem(itemToGift, 1);
            player.setEnergy(player.getEnergy() - energyCost);
            gameTime.addTime(timeCostInMinute);

            System.out.println("Kamu memberikan " + itemToGift.getName() + " kepada " + npc.getName() + ".");


            boolean isLoved = npc.getLoveditems() != null && npc.getLoveditems().stream().anyMatch(lovedItem -> lovedItem.getName().equalsIgnoreCase(itemToGift.getName()));
            boolean isLiked = npc.getLikeditems() != null && npc.getLikeditems().stream().anyMatch(likedItem -> likedItem.getName().equalsIgnoreCase(itemToGift.getName()));
            boolean isHated = npc.getHateditems() != null && npc.getHateditems().stream().anyMatch(hatedItem -> hatedItem.getName().equalsIgnoreCase(itemToGift.getName()));

            if (npc.getName().equals("Mayor Tadi") && !isLoved && !isLiked) {
                isHated = true; 
            }

            if (npc.getName().equals("Perry") && itemToGift instanceof items.Fish) {
                 isHated = true;
                 isLoved = false;
                 isLiked = false;
            }
            if (npc.getName().equals("Emily") && itemToGift instanceof items.Seeds) {
                isLoved = true;
                isHated = false; 
                isLiked = false;
            }


            if (isLoved) {
                npc.setHeartpoints(npc.getHeartpoints() + 25);
                System.out.println(npc.getName() + " sangat menyukai hadiahmu! Heartpoint +25");
            } else if (isLiked) {
                npc.setHeartpoints(npc.getHeartpoints() + 20);
                System.out.println(npc.getName() + " menyukai hadiahmu. Heartpoint +20");
            } else if (isHated) {
                npc.setHeartpoints(npc.getHeartpoints() - 25);
                System.out.println(npc.getName() + " membenci hadiahmu... Heartpoint -25");
            } else {
                System.out.println(npc.getName() + " netral terhadap hadiah ini."); 
            }
            System.out.println("Heart points " + npc.getName() + " sekarang: " + npc.getHeartpoints());
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        } else {
            System.out.println("Energi tidak cukup untuk memberikan hadiah.");
            try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
        }
    }
    private void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                for (int i = 0; i < 50; ++i) System.out.println();
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}