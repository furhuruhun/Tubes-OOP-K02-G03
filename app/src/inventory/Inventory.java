import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Item, Integer> items;

    public Inventory() {
        items = new HashMap<>();
    }

    public void addItem(Item item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    public void removeItem(Item item, int quantity) {
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
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            sb.append(entry.getKey().getName())
              .append(" x")
              .append(entry.getValue())
              .append("\n");
        }
        return sb.toString();
    }

    public Map<Item, Integer> getItemsMap() {
        return items;
    }
}
