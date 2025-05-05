import java.util.List;

public class House {

    public House() {
    }

    public boolean cook(Player owner, Recipe recipe) {
        List<Item> ingredients = recipe.getBahanBaku();

        if (!owner.getInventory().haveitem(ingredients)) {
            System.out.println("Bahan tidak cukup");
            return false;
        }

        owner.getInventory().removeItem(ingredients);
        owner.getInventory().addItem(recipe.getResult(), 1);
        System.out.println("Berhasil memasak: " + recipe.getResult().getName());
        return true;
    }

    public void rest(Player owner) {
        owner.restoreenergy(owner.getMaxEnergy());
        System.out.println("Kamu beristirahat dan memulihkan energimu");
    }

    public void watchTV() {
        System.out.println("Kamu menonton TV.");
    }
}
