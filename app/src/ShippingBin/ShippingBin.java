package ShippingBin;

import java.util.HashMap;
import java.util.Map;

import Player.Location;
import Player.Player;
import items.BuyableItems;
import items.Items;

public class ShippingBin {
    private final int MAX_SLOTS = 16;
    private final Map<Items, Integer> bin;
    private boolean soldToday;
    private Location location;

    public ShippingBin() {
        this.bin = new HashMap<>();
        this.soldToday = false;
    }

    public int getMaxSlots() {
        return MAX_SLOTS;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location=location;
    }

    public Map<Items, Integer> getBin() {
        return bin;
    }

    

    /**
     * Menambah item ke shipping bin.
     * Jika sudah terdapat pada bin, tambah quantity. 
     * Item tidak bisa dikembalikan ke inventory.
     */
    public void addItem(Items item, int quantity, Player player) {
        if (item == null || quantity <= 0) {
            System.out.println("Item atau kuantitas tidak valid.");
            return;
        }

        // Cek apakah item tersedia dan cukup
        if (!player.getInventory().checkItem(item) || !player.getInventory().hasSufficientItem(item, quantity)) {
            System.out.println("Item tidak tersedia di inventory atau kuantitas kurang.");
            return;
        }

        boolean itemAlreadyInBin = bin.containsKey(item);

        if (itemAlreadyInBin) {
            // Tambah jumlah ke bin
            player.getInventory().removeItem(item, quantity);
            int currentQty = bin.get(item);
            bin.put(item, currentQty + quantity);
        } else if (bin.size() < MAX_SLOTS) {
            // Slot baru
            player.getInventory().removeItem(item, quantity);
            bin.put(item, quantity);
        } else {
            System.out.println("Shipping bin penuh! Tidak bisa menambahkan item baru.");
        }
    }

    /**
     * Menjual semua item dalam bin jika belum pernah dijual hari ini.
     * Pemanggilan dilakukan saat player tidur (sleep).
     * Gold ditambahkan ke player.
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
            .mapToInt(entry -> ((BuyableItems)entry.getKey()).getHargaJual() * entry.getValue())
            .sum();

        player.setGold(totalSellPrice);
        System.out.println("Item berhasil dijual. Total gold diterima: " + totalSellPrice);
        clearBin();
        soldToday = true;
    }

    /**
     * Reset status penjualan harian (dipanggil saat hari baru).
     */
    public void resetBinDay() {
        soldToday = false;
    }

    /**
     * Menghapus semua item dalam shipping bin (setelah dijual).
     */
    private void clearBin() {
        bin.clear();
    }

    /**
     * Menampilkan isi shipping bin dan total harga jual.
     */
    public void printBin() {
        if (bin.isEmpty()) {
            System.out.println("Shipping bin kosong.");
            return;
        }

        System.out.printf("%-20s %-10s %-10s\n", "Item", "Qty", "Total");
        System.out.println("----------------------------------------");

        int totalSellPrice = 0;
        for (Map.Entry<Items, Integer> entry : bin.entrySet()) {
            Items item = entry.getKey();
            int quantity = entry.getValue();
            int itemPrice = ((BuyableItems)item).getHargaJual();
            int totalItemPrice = itemPrice * quantity;
            totalSellPrice += totalItemPrice;

            System.out.printf("%-20s %-10d %-10d\n", item.getName(), quantity, totalItemPrice);
        }

        System.out.println("----------------------------------------");
        System.out.println("Total Sell Price: " + totalSellPrice);
    }
}