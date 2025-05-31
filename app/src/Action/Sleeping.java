package Action;

import GameCalendar.Model.GameTime;
import House.House;
import Player.Location;
import Player.Player;
import farm.FarmMap.FarmMap;

public class Sleeping extends Action{
    public Sleeping() {
        super(0,0);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        FarmMap farmMap = player.getFarmname().getFarmMap();
        Location playerLocationInFarm = player.getLocation_infarm();
        String playerLocationInWorld = player.getLocation_inworld();

        if(playerLocationInWorld.equalsIgnoreCase("Farm")){
            if(playerLocationInFarm == null || !farmMap.isPlayerInHouse(playerLocationInFarm)){
                System.out.println("Kamu harus berada di dalam rumah untuk bisa tidur di farm!");
                            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;
            }
        }
        else if(!playerLocationInWorld.equalsIgnoreCase("Farm")){
            if(player.getEnergy() != -20){
                System.out.println("Kamu harus berada di farm untuk bisa tidur!");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return;
            }
        }

        if(player.getEnergy() < 10 && player.getEnergy() > -20){
            player.setEnergy(player.getEnergy() + 50);
        }
        else if(player.getEnergy() == 0){
            player.setEnergy(player.getEnergy() + 10);
        }
        else{
            player.setEnergy(100);
        }

        gameTime.timeSkip(6);
        Location oldPlayerLocationOnFarm = null;
        if(playerLocationInWorld.equalsIgnoreCase("Farm") && playerLocationInFarm != null){
            oldPlayerLocationOnFarm = playerLocationInFarm;
        }

        player.setLocation_inworld("Farm");
        Location houseLoc = player.getFarmname().getFarmMap().getHouse().getLocation();

        if(houseLoc != null){
            int bangunX = houseLoc.getX() + (House.DEFAULT_WIDTH / 2);
            int bangunY = houseLoc.getY() + House.DEFAULT_HEIGHT;

            if (bangunY >= 32){
                bangunY = 31;
            }

            if (bangunX >= 32){
                bangunX = 31;
            }

            Location lokasibangun = new Location(bangunX, bangunY);
            if(oldPlayerLocationOnFarm != null && farmMap.getTile(oldPlayerLocationOnFarm) != null){
                farmMap.getTile(oldPlayerLocationOnFarm).setSymbol('.');
            }
            
            player.setLocation_infarm(lokasibangun);
            
            if(farmMap.getTile(lokasibangun) != null){
                farmMap.getTile(lokasibangun).setSymbol('p');
            }
        }
        else{
            player.setLocation_infarm(new Location(10, 10));
             if(farmMap.getTile(new Location(10,10)) != null){
                 farmMap.getTile(new Location(10,10)).setSymbol('p');
             }
        }
        System.out.println("Selamat pagi! Hari baru telah tiba.");
        System.out.println("Energi kamu telah dipulihkan.");
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

}