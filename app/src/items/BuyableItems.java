package items;
public class BuyableItems extends Items {
    private int hargaJual;
    private int hargaBeli;

    public BuyableItems(String name, int hargaJual, int hargaBeli) {
        super(name);
        this.hargaJual = hargaJual;
        this.hargaBeli = hargaBeli;
    }
    public int getHargaJual() {
        return hargaJual;
    }
    public void setHargaJual(int hargaJual) {
        this.hargaJual = hargaJual;
    }
    public int getHargaBeli() {
        return hargaBeli;
    }
    public void setHargaBeli(int hargaBeli) {
        this.hargaBeli = hargaBeli;
    }
}
