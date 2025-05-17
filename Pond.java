public class Pond{
    private String name;
    
    public Pond(){
        this.name = "Pond";
    }

    public void greet() {
        System.out.println("Welcome to the " + name + "!");
    }
    public String getName() {
        return name;
    }
}