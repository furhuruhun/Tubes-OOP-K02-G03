package inventory;
import java.util.HashMap;
import java.util.Map;

import items.Items;
public class Inventory {
    private Map<Items, Integer> items;

    public Inventory() {
        items = new HashMap<>();
    }

    public void addItem(Items item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(Items item, int quantity) {
        if (items.containsKey(item)) {
            int current = items.get(item);
            if (quantity >= current) {
                items.remove(item);
            } else {
                items.put(item, current - quantity);
            }
        }
    }

    public String getItems() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Items, Integer> entry : items.entrySet()) {
            sb.append(entry.getKey().getName())
              .append(" x")
              .append(entry.getValue())
              .append("\n");
        }
        return sb.toString();
    }

        public String printInventory() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Items, Integer> entry : items.entrySet()) {
            sb.append(entry.getKey().getName())
              .append(" x")
              .append(entry.getValue())
              .append("\n");
        }
        return sb.toString();
    }

    public Map<Items, Integer> getItemsMap() {
        return items;
    }

    public boolean checkItem(Items item) {
        return items.containsKey(item);
    }

    public boolean hasSufficientItem(Items item, int quantity) {
        return items.containsKey(item) && items.get(item) >= quantity;
    }

}
