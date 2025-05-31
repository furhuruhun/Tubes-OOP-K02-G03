package Action;
import java.util.Map;
import java.util.List; 
import java.util.ArrayList; 

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
import farm.Farm;
import items.Items;
import items.Fish; 
import items.Misc;
import items.Recipe;


public class Cooking extends Action {
    private Recipe recipe;
    private String fuelType;

    public Cooking(Recipe recipe, String fuelType) {
        super(10, 60); // 10 energi, 60 menit
        this.recipe = recipe;
        this.fuelType = fuelType;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        Farm farm = player.getFarmname();

        if (!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Memasak hanya bisa dilakukan di dalam rumah.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }
        Location loc = player.getLocation_infarm();
        if (farm == null || farm.getFarmMap() == null || !farm.getFarmMap().isPlayerInHouse(loc)) {
            System.out.println("Kamu harus berada di dalam rumah untuk memasak.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        if (player.getEnergy() < -20 + energyCost) {
            System.out.println("Energi tidak cukup untuk memasak.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        Items fuelItem = null;
        if (fuelType.equalsIgnoreCase("Firewood")) {
            for (Items item : player.getInventory().getItemsMap().keySet()) {
                if (item.getName().equalsIgnoreCase("Firewood")) {
                    fuelItem = item;
                    break;
                }
            }
            if (fuelItem == null) fuelItem = new Misc("Firewood", 0, 0); 

        } else if (fuelType.equalsIgnoreCase("Coal")) {
            for (Items item : player.getInventory().getItemsMap().keySet()) {
                if (item.getName().equalsIgnoreCase("Coal")) {
                    fuelItem = item;
                    break;
                }
            }
            if (fuelItem == null) fuelItem = new Misc("Coal", 0, 0); 
        } else {
            System.out.println("Jenis fuel tidak dikenal.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }


        boolean hasEnoughFuel = player.getInventory().hasSufficientItem(fuelItem, 1);

        if (!hasEnoughFuel) {
            System.out.println("Jumlah " + fuelItem.getName() + " tidak cukup atau tidak ada di inventory.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int requiredQuantity = entry.getValue();

            if (requiredItem.getName().equalsIgnoreCase("AnyFishPlaceholder")) {
                int fishCount = 0;
                for (Map.Entry<Items, Integer> invEntry : player.getInventory().getItemsMap().entrySet()) {
                    if (invEntry.getKey() instanceof Fish) {
                        fishCount += invEntry.getValue();
                    }
                }
                if (fishCount < requiredQuantity) {
                    System.out.println("Bahan tidak cukup: memerlukan " + requiredQuantity + " ikan jenis apa saja, kamu punya " + fishCount + ".");
                    try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    return;
                }
            } else if (requiredItem.getName().equalsIgnoreCase("LegendFishPlaceholder")) {
                 int legendFishCount = 0;
                for (Map.Entry<Items, Integer> invEntry : player.getInventory().getItemsMap().entrySet()) {
                    if (invEntry.getKey() instanceof Fish && ((Fish) invEntry.getKey()).getFishType().equalsIgnoreCase("Legendary")) {
                        legendFishCount += invEntry.getValue();
                    }
                }
                if (legendFishCount < requiredQuantity) {
                    System.out.println("Bahan tidak cukup: memerlukan " + requiredQuantity + " ikan legendaris, kamu punya " + legendFishCount + ".");
                    try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    return;
                }
            }
            else {
                if (!player.getInventory().hasSufficientItem(requiredItem, requiredQuantity)) {
                    System.out.println("Bahan tidak cukup untuk memasak " + recipe.getName() + ". Kekurangan: " + requiredItem.getName());
                    try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    return;
                }
            }
        }

        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            Items requiredItem = entry.getKey();
            int quantityToConsume = entry.getValue();

            if (requiredItem.getName().equalsIgnoreCase("AnyFishPlaceholder")) {
                int consumedCount = 0;
                List<Items> fishToRemoveFromInventory = new ArrayList<>();

                for (Map.Entry<Items, Integer> invEntry : player.getInventory().getItemsMap().entrySet()) {
                    if (consumedCount >= quantityToConsume) break;
                    if (invEntry.getKey() instanceof Fish && !((Fish) invEntry.getKey()).getFishType().equalsIgnoreCase("Legendary")) {
                        Items fishInStack = invEntry.getKey();
                        int availableInStack = invEntry.getValue();
                        int canConsumeFromThisStack = Math.min(availableInStack, quantityToConsume - consumedCount);
                        
                        for(int i=0; i<canConsumeFromThisStack; i++) fishToRemoveFromInventory.add(fishInStack);
                        consumedCount += canConsumeFromThisStack;
                    }
                }
                if (consumedCount < quantityToConsume) {
                    for (Map.Entry<Items, Integer> invEntry : player.getInventory().getItemsMap().entrySet()) {
                        if (consumedCount >= quantityToConsume) break;
                        if (invEntry.getKey() instanceof Fish && ((Fish) invEntry.getKey()).getFishType().equalsIgnoreCase("Legendary")) {
                           Items fishInStack = invEntry.getKey();
                            int availableInStack = invEntry.getValue();
                            int canConsumeFromThisStack = Math.min(availableInStack, quantityToConsume - consumedCount);
                            
                            for(int i=0; i<canConsumeFromThisStack; i++) fishToRemoveFromInventory.add(fishInStack);
                            consumedCount += canConsumeFromThisStack;
                        }
                    }
                }
                for(Items fishItem : fishToRemoveFromInventory){
                    player.removefromInventory(fishItem, 1);
                }

            } else if (requiredItem.getName().equalsIgnoreCase("LegendFishPlaceholder")) {
                int consumedCount = 0;
                List<Items> legendFishToRemoveFromInventory = new ArrayList<>();
                 for (Map.Entry<Items, Integer> invEntry : player.getInventory().getItemsMap().entrySet()) {
                    if (consumedCount >= quantityToConsume) break;
                     if (invEntry.getKey() instanceof Fish && ((Fish) invEntry.getKey()).getFishType().equalsIgnoreCase("Legendary")) {
                        Items fishInStack = invEntry.getKey();
                        int availableInStack = invEntry.getValue();
                        int canConsumeFromThisStack = Math.min(availableInStack, quantityToConsume - consumedCount);
                        
                        for(int i=0; i<canConsumeFromThisStack; i++) legendFishToRemoveFromInventory.add(fishInStack);
                        consumedCount += canConsumeFromThisStack;
                    }
                }
                for(Items fishItem : legendFishToRemoveFromInventory){
                    player.removefromInventory(fishItem, 1);
                }
            }
            else {
                player.removefromInventory(requiredItem, quantityToConsume);
            }
        }
        player.removefromInventory(fuelItem, 1);

        player.setEnergy(player.getEnergy() - energyCost);
        gameTime.addTime(timeCostInMinute);

        System.out.println("Memasak " + recipe.getName() + " secara paralel...");

        Thread cookingThread = new Thread(() -> {
            try {
                long sleepDurationMillis = 12000; 
                
                System.out.println("(Proses memasak " + recipe.getName() + " akan selesai dalam " + (sleepDurationMillis / 1000) + " detik waktu nyata...)");
                Thread.sleep(sleepDurationMillis);
                
                synchronized (player) { 
                    if (recipe.getResultFood() != null) {
                        player.addInventory(recipe.getResultFood(), 1); 
                        System.out.println("\n==== PEMBERITAHUAN MASAKAN ====");
                        System.out.println("MASAKAN SELESAI: " + recipe.getName() + " telah ditambahkan ke inventory!");
                        System.out.println("===============================");
                    } else {
                         System.err.println("\nError saat memasak " + recipe.getName() + ": Hasil masakan tidak terdefinisi.");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                System.out.println("\nProses memasak " + recipe.getName() + " terganggu atau dibatalkan.");
            }
        });
        cookingThread.setDaemon(true); 
        cookingThread.start();
    }
}
            
        