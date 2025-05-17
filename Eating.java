import java.util.Scanner;

public class Eating extends Action {
    public Eating() {
        super(0, 5);
    }
    @Override
    public void perform(Player player, GameTime gameTime) {
        // Check if the player has food in their inventory
        boolean hasFood = false;
        for (Items item : player.getInventory().getItemsMap().keySet()) {
            if (item instanceof Food) {
                hasFood = true;
                break;
            }
        }
        if (!hasFood) {
            System.out.println("You don't have any food to eat!");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the food you want to eat:");
        String foodName = scanner.nextLine();
        Food foodToEat = null;
        for (Items item : player.getInventory().getItemsMap().keySet()) {
            if (item instanceof Food && item.getName().equals(foodName)) {
                foodToEat = (Food) item;
                break;
            }
        }
        if (foodToEat != null) {
            // Check if the player has enough energy to eat
            player.setEnergy(player.getEnergy() + foodToEat.getEnergy());
            player.removefromInventory(foodToEat, 1); // remove food from inventory
            if (player.getEnergy() > player.getMaxEnergy()) {
                player.setEnergy(player.getMaxEnergy());
            }
            System.out.println("You have eaten the " + foodToEat.getName());
            System.out.println("Your energy is now: " + player.getEnergy());
            gameTime.addTime(5);
        } else {
            System.out.println("Invalid food item.");
        }
        scanner.close();
    }
}
