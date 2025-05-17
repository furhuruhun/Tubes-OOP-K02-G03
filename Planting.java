import java.util.Scanner;

public class Planting extends Action {
    public Planting(){
        super(5, 5);
    }
    @Override
    public void perform(Player player, GameTime gameTime) {
        boolean hasSeeds = false;

        for (Items item : player.getInventory().getItemsMap().keySet()) {
            if (item instanceof Seeds) {
                hasSeeds = true;
                break;
            }
        }

        if (hasSeeds) {
            if (player.getFarmname().getFarmMap().isTillable(player.getLocation_infarm())) { //cek tile di posisi player bisa di plant apa ngga
                if (player.getEnergy() >= -15) { //cek energi
                    // nanti disini mau ditambahin pake moving
                    //
                    //
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the seeds you want to plant:");
                    String seedName = scanner.nextLine();
                    Seeds seedToPlant = null;
                    for (Items item : player.getInventory().getItemsMap().keySet()) {
                        if (item instanceof Seeds && item.getName().equals(seedName)) {
                            seedToPlant = (Seeds) item;
                            break;
                        }
                    }
                    if (seedToPlant != null) {
                        // check if the season is correct
                        if(gameTime.getCurrSeason() != (seedToPlant.getSeason())) {
                            System.out.println("You cannot plant this seed in the current season.");
                            scanner.close();
                            return;
                        }
                        player.setLocation_infarm(new Location(player.getLocation_infarm().getX() - 1, player.getLocation_infarm().getY()));
                        player.getFarmname().getFarmMap().placePlayer(player.getLocation_infarm());
                        player.getFarmname().getFarmMap().getTile(player.getLocation_infarm()).setSymbol('l');
                        player.getInventory().removeItem(seedToPlant, 1); // remove seeds from inventory
                        System.out.println("You have planted the seeds");
                        player.setEnergy(player.getEnergy() - 5);
                        gameTime.addTime(5);
                        if (player.getEnergy() <= -20){
                            System.out.println("You have no energy left, you will be sleeping now.");
                            Sleeping sleep = new Sleeping();
                            sleep.perform(player, gameTime);
                            System.out.println("You have slept");

                        }
                    } else {
                        System.out.println("Invalid seed name");
                    }
                    scanner.close();
                } else {
                    System.out.println("You don't have enough energy to plant the seeds");
                }
            } else {
                System.out.println("This tile is not plantable");
            }
        } 
        else {
            System.out.println("Tidak ada seeds.");
        }
    }
    
}
