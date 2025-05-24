package House;

import Player.*;

public class House {
    private String name;
    private String owner;
    private Location location;
    
    public House(Player owner) {
        this.owner = owner.getName();
        this.name = owner.getName() + "'s House";
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

//     public boolean cook(Player owner, Recipe recipe) {
//         List<Item> ingredients = recipe.getBahanBaku();

//         if (!owner.getInventory().haveitem(ingredients)) {
//             System.out.println("Bahan tidak cukup");
//             return false;
//         }

//         owner.getInventory().removeItem(ingredients);
//         owner.getInventory().addItem(recipe.getResult(), 1);
//         System.out.println("Berhasil memasak: " + recipe.getResult().getName());
//         return true;
//     }

//     public void rest(Player owner) {
//         owner.setEnergy(owner.getMaxEnergy());
//         System.out.println("Kamu beristirahat dan memulihkan energimu");
//     }

//     public void watchTV() {
//         System.out.println("Kamu menonton TV.");
//     }
}
