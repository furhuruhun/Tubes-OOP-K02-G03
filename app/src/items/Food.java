package items;
public class Food extends BuyableItems{
    private int energy;

    public Food(String name, int energy, int hargaJual, int hargaBeli) {
        super(name, hargaJual, hargaBeli);
        this.energy = energy;
    }
    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
