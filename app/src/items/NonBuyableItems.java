package items;
public class NonBuyableItems extends Items {
    public NonBuyableItems(String name) {
        super(name);
    }

    @Override
    public int getSellPrice() {
        return 0;
    }

    @Override
    public boolean isSellable() {
        return false;
    }
}
