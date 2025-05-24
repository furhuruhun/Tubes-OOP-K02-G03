package Action;

import java.util.Map;

import GameCalendar.Model.GameTime;
import Player.Location;
import Player.Player;
import farm.Farm;
import items.Items;
import items.Misc;
import items.Recipe;



public class Cooking extends Action {
    //ini masalah di concurrency nya brou (blm ngrti)
    private Recipe recipe;
    private String fuelType;

    public Cooking(Recipe recipe, String fuelType) {
        super(10, 60);
        this.recipe = recipe;
        this.fuelType = fuelType;
    }

    @Override
    public void perform(Player player, GameTime gametime) {
        Farm farm = player.getFarmname();

        if (player.getLocation_infarm() == null) {
            System.out.println("Memasak hanya bisa dilakukan di dalam farm.");
            return;
        }

        Location loc = player.getLocation_infarm();
        if (!farm.getFarmMap().isPlayerInHouse(loc)) {
            System.out.println("Kamu harus berada di dalam rumah untuk memasak.");
            return;
        }

        if (player.getEnergy() < energyCost) {
            System.out.println("Energi tidak cukup untuk memasak.");
            return;
        }

        Items fuelItem = null;
        int fuelCapacity = 0;
        if (fuelType.equalsIgnoreCase("Firewood")) {
            for(Items item : player.getInventory().getItemsMap().keySet()) {
                if(item instanceof Misc && item.getName().equalsIgnoreCase("Firewood")) {
                    fuelItem = item;
                    player.getInventory().removeItem(fuelItem, 1);
                    break;
                }
            }
            fuelCapacity = 1;
        } else if (fuelType.equalsIgnoreCase("Coal")) {
            for(Items item : player.getInventory().getItemsMap().keySet()) {
                if(item instanceof Misc && item.getName().equalsIgnoreCase("Firewood")) {
                    fuelItem = item;
                    player.getInventory().removeItem(fuelItem, 1);
                    break;
                }
            }
            fuelCapacity = 2;
        } else {
            System.out.println("Jenis fuel tidak dikenal.");
            return;
        }

        if (!player.haveitem(fuelItem)) {
            System.out.println("Fuel tidak tersedia di inventory.");
            return;
        }

        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!player.getInventory().getItemsMap().containsKey(entry.getKey()) || player.getInventory().getItemsMap().get(entry.getKey()) < entry.getValue()) {
                System.out.println("Bahan tidak cukup untuk memasak " + recipe.getName());
                return;
            }
        }

        for (Map.Entry<Items, Integer> entry : recipe.getIngredients().entrySet()) {
            player.removefromInventory(entry.getKey(), entry.getValue());
        }
        player.removefromInventory(fuelItem, 1);

        player.setEnergy(player.getEnergy() - energyCost);
        System.out.println("Memasak " + recipe.getName() + " selama 1 jam...");
        gametime.addTime(timeCostInMinute);

        player.addInventory(recipe.getResultFood());
        System.out.println("Selesai! " + recipe.getName() + " telah masuk ke inventory.");
    }
}