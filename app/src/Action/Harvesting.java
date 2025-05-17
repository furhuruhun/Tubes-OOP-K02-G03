package Action;

import Player.Player;
import items.Crops;
import items.Seeds;
import src.farmmap.Tile;

public class Harvesting extends Action{
    public Harvesting() {
        super(5, 5);
    }

    @Override
    public void perform(Player player, Gametime gametime) {
        //asumsi ini tiap perform cuma sekali ambil panen-an

        //cek apakah tiles yang dimaksud bernilai (l)
        if(player.getFarmname().getFarmMap().isPlanted(player.getLocation_infarm())) {
            Seeds seed = player.getFarmname().getFarmMap().getTile(player.getLocation_infarm()).getPlantedSeed();

            //convert crops dri seed
            Crops harvestedCrop = new Crops(
                seed.getName(),
                seed.getHargaJual(),
                seed.getHargaBeli(),
                1 //jujur ini nanti bingung gimana ngubah ngubah biar sesuai spek
            );

            player.getInventory().addItem(harvestedCrop, harvestedCrop.getJumlahperpanen()); // mang bisa ya parameter nya item, tpi yg di-add crops tipe nya
            player.getFarmname().getFarmMap().getTile(player.getLocation_infarm()).clearTile();

            player.setEnergy(player.getEnergy() - this.energycost);
            gametime.addMinutes(this.timeCostInMinute);
            
            System.out.println("Berhasil memanen " + harvestedCrop.getName() + " x" + harvestedCrop.getJumlahperpanen());
        } else {
            System.out.println("Huu, orang anda gapunya taneman dan tanahnya ga ditanamin juga wlek");
        }

    }
}
