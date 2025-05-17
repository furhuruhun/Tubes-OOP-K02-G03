public class Sleeping extends Action{
    public Sleeping() {
        super(0, 0);
    }
    @Override
    public void perform(Player player, GameTime gameTime) {
        // Set the player's energy to the maximum value
        if(player.getEnergy() < 10 && player.getEnergy() > 0){
            player.setEnergy(player.getEnergy() + 50);
        }
        else if(player.getEnergy() == 0){
            player.setEnergy(10);
        }
        else{
            player.setEnergy(player.getMaxEnergy());
        }
        // Set the player's time to the next day
        gameTime.timeSkip(6);
        System.out.println("Good morning! It's a new day.");
        System.out.println("You have slept and regained your energy.");
    }
    
}
