package items;

import java.util.Objects;

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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Items items = (Items) o;
        return Objects.equals(name, items.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
