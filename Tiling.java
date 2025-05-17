public class Tiling extends Action{
    public Tiling() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        if(player.haveitem(new Equipment("hoe"))){ //cek punya hoe apa ngga

            if(player.getFarmname().getFarmMap().isEmpty(player.getLocation_infarm())){ //cek tile di posisi player bisa di tiling apa ngga

                if(player.getEnergy() >= 5){ //cek energi
                    player.setLocation_infarm(new Location(player.getLocation_infarm().getX() - 1, player.getLocation_infarm().getY()));
                    player.getFarmname().getFarmMap().placePlayer(player.getLocation_infarm());
                    player.getFarmname().getFarmMap().getTile(player.getLocation_infarm()).setSymbol('t');
                    System.out.println("You have tilled the soil");
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
                    System.out.println("You don't have enough energy to till the soil");
                }    
            }
            else{
                System.out.println("This tile is not tillable");
            }
        }
        else{
            System.out.println("You need a hoe to till this tile");
        }
    }
}
