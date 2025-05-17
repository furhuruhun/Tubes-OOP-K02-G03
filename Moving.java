import java.util.Scanner;

public class Moving extends Action{
    public Moving(){
        super(0, 0);
    }
    @Override
    public void perform(Player player, GameTime gameTime){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Where do you want to move?");
        System.out.println("1. House");
        System.out.println("2. Pond");
        System.out.println("3. Shipping Bin");
        System.out.println("4. World");
        System.out.println("5. Custom Coordinates");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
            case "House":
                System.out.println("You have moved to the House");
                player.setLocation_infarm(player.getFarmname().getFarmMap().getHouse().getLocation());
                break;
            case "2":
            case "Pond":
                System.out.println("You have moved to the Pond");
                player.setLocation_infarm(player.getFarmname().getFarmMap().getPond().getLocation());
                break;
            case "3":
            case "Shipping Bin":
                System.out.println("You have moved to the Shipping Bin");
                player.setLocation_infarm(player.getFarmname().getFarmMap().getShippingBin().getLocation());
                break;
            case "4":
            case "World":
                System.out.println("You have moved to the World");
                player.setLocation_infarm(new Location(0, 0));
                break;
            case "5":
            case "Custom Coordinates":
                System.out.println("Enter the coordinates (x): ");
                String xCoordinate = scanner.nextLine();
                System.out.println("Enter the coordinates (y): ");
                String yCoordinate = scanner.nextLine();
                int x = Integer.parseInt(xCoordinate);
                int y = Integer.parseInt(yCoordinate);
                player.setLocation_infarm(new Location(x, y));
                player.getFarmname().getFarmMap().placePlayer(new Location(x, y));
                System.out.println("You have moved to the coordinates: " + x + ", " + y);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }

    } 
}