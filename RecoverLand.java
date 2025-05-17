public class RecoverLand extends Action{
    public RecoverLand(){
        super(5, 5);
    }
    @Override
    public void perform(Player player, GameTime gameTime) {
        if(player.haveitem(new Equipment("pickaxe"))){ //cek punya hoe apa ngga

            if(player.getFarmname().getFarmMap().isTillable(player.getLocation_infarm())){ //cek tile di posisi player bisa di recover apa ngga

                if(player.getEnergy() >= 5){ //cek energi
                    player.setLocation_infarm(new Location(player.getLocation_infarm().getX() - 1, player.getLocation_infarm().getY()));
                    player.getFarmname().getFarmMap().placePlayer(player.getLocation_infarm());
                    player.getFarmname().getFarmMap().getTile(player.getLocation_infarm()).setSymbol('.');
                    System.out.println("You have recovered the land");
                    player.setEnergy(player.getEnergy() - 5);
                    gameTime.addTime(5);
                    if (player.getEnergy() <= -20){
                            System.out.println("You have no energy left, you will be sleeping now.");
                            Sleeping sleep = new Sleeping();
                            sleep.perform(player, gameTime);
                            System.out.println("You have slept");

                    }
                } 
                else {
                    System.out.println("You don't have enough energy to recover the land");
                }    
            }
            else{
                System.out.println("This tile is not recoverable");
            }
        }
        else{
            System.out.println("You need a pickaxe to recover this tile");
        }
    }

}