package Action;

import GameCalendar.Model.GameTime;

public class Selling extends Action {
    private ShippingBin bin;
    private Item itemToSell;
    private int quantity;

    public Selling(ShippingBin bin, Item itemToSell, int quantity) {
        super(0, 15); // 15 menit waktu, tanpa energi
        this.bin = bin;
        this.itemToSell = itemToSell;
        this.quantity = quantity;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        bin.addItem(itemToSell, quantity, player);
        gameTime.addTime(timeCostInMinute);
        System.out.println("Kamu memasukkan " + quantity + " " + itemToSell.getName() + " ke dalam shipping bin.");
    }
}