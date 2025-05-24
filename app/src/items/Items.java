package items;
public class Items {
    private String name;

    public Items(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getSellPrice() {
        return 0;
    }

    public boolean isSellable() {
        return false;
    }
}
