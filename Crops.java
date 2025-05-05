public class Crops extends BuyableItems {
    private int jumlahperpanen;
    
    public Crops(String name, int hargaJual, int hargaBeli, int jumlahperpanen) {
        super(name, hargaJual, hargaBeli);
        this.jumlahperpanen = jumlahperpanen;
    }
    public int getJumlahperpanen() {
        return jumlahperpanen;
    }
    public void setJumlahperpanen(int jumlahperpanen) {
        this.jumlahperpanen = jumlahperpanen;
    }
}
