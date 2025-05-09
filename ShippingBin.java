import java.util.HashMap;
import java.util.Map;

public class ShippingBin {
    private final int MAX_SLOTS = 16;
    private final Map<Item, Float> bin;
    private boolean soldToday;

    public ShippingBin() {
        this.bin = new HashMap<>();
        this.soldToday = false;
    }

    public int getMaxSlots() {
        return MAX_SLOTS;
    }

    /**
     * Menambah item ke shipping bin.
     * Jika sudah terdapat pada bin, tambah quantity. 
     */
    public void addItem(Item item, int quantity, Player player) {
        if (item == null || quantity <= 0) {
            System.out.println("Item atau kuantitas tidak valid.");
            return;
        }

        if (!player.inventory.checkItem(item) || !player.inventory.hasSufficientItem(item, quantity)) {
            System.out.println("Item tidak tersedia di inventory atau kuantitas kurang.");
            return;
        }

        boolean itemAlreadyInBin = bin.containsKey(item);

        if (itemAlreadyInBin) {
            player.inventory.removeItem(item, quantity);
            int currentQty = bin.get(item);
            bin.put(item, currentQty + quantity);
        } else if (bin.size() < MAX_SLOTS) {
            player.inventory.removeItem(item, quantity);
            bin.put(item, quantity);
        } else {
            System.out.println("Shipping bin penuh! Tidak bisa menambahkan item baru.");
        }
    }

    /**
     * Menjual semua item dalam bin jika belum pernah dijual hari ini.
     * Pemanggilan dari fungsi sleep().
     */
    public void sellBin(Player player) {
        if (soldToday) {
            System.out.println("Kamu sudah menjual hari ini. Tunggu besok.");
            return;
        }

        if (bin.isEmpty()) {
            System.out.println("Shipping bin kosong. Tidak ada item untuk dijual.");
            return;
        }

        int totalSellPrice = bin.entrySet().stream()
            .mapToInt(entry -> entry.getKey().getSellPrice() * entry.getValue())
            .sum();

        player.addGold(totalSellPrice);
        System.out.println("Item berhasil dijual. Total gold diterima: " + totalSellPrice);
        clearBin();
        soldToday = true;
    }

    /**
     * Reset status penjualan harian (dipanggil oleh sistem saat hari baru).
     */
    public void resetBinDay() {
        soldToday = false;
    }

    /**
     * Clear semua item dalam shipping bin.
     */
    public void clearBin() {
        bin.clear();
    }

    /**
     * Menampilkan semua item yang ada pada shipping bin
     */
    public void printBin() {
        if (bin.isEmpty()) {
            System.out.println("Shipping bin kosong.");
            return;
        }

        System.out.printf("%-15s %-10s\n", "Item", "Quantity");
        System.out.println("-----------------------------");

        int totalSellPrice = 0;
        for (Map.Entry<Item, Float> entry : bin.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            int itemPrice = item.getSellPrice();
            totalSellPrice += itemPrice * quantity;

            System.out.printf("%-15s %-10d\n", item.getName(), quantity);
        }

        System.out.println("Total Sell Price: " + totalSellPrice);
    }
}
