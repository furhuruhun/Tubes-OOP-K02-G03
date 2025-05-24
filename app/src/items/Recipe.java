package items;

import java.util.Map;

public class Recipe extends NonBuyableItems {
    private boolean status = false;
    private Map<Items, Integer> ingredients;
    private Items resultFood;

    public Recipe(String name, Map<Items, Integer> ingredients, Items resultFood) {
        super(name);
        this.ingredients = ingredients;
        this.resultFood = resultFood;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Map<Items, Integer> getIngredients() {
        return ingredients;
    }

    public Items getResultFood() {
        return resultFood;
    }

    public boolean canCook(inventory.Inventory inv) {
        for (Map.Entry<Items, Integer> entry : ingredients.entrySet()) {
            if (!inv.hasSufficientItem(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}
