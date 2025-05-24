package Action;

public class Eating extends Action {
    private Item food;

    public Eating(Item food) {
        super(0, 5);
        this.food = food;
    }

    @Override
    public void perform(Player player) {
        if (player.getEnergy() >= 100) {
            System.out.println("Energi sudah penuh.");
            return;
        }

        if (!player.haveitem(food)) {
            System.out.println("Kamu tidak punya makanan tersebut.");
            return;
        }
        if (!(food instanceof Edible)) {
            System.out.println("Item ini tidak bisa dimakan.");
            return;
        }
        int energyGain = ((Edible) food).getEnergyValue();
        player.setEnergy(player.getEnergy() + energyGain);
        player.removefromInventory(food, 1);
        player.getFarmname().getTime().addMinutes(timeCostInMinute);
        System.out.println("Kamu memakan " + food.getName() + ", energi bertambah +" + energyGain);
    }
}