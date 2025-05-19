public class Cooking extends Action {
    private Recipe recipe;
    private String fuelType;

    public Cooking(Recipe recipe, String fuelType) {
        super(10, 60);
        this.recipe = recipe;
        this.fuelType = fuelType;
    }

    @Override
    public void perform(Player player) {
        Farm farm = player.getFarmname();

        if (!player.getLocation_inworld().equals("Farm")) {
            System.out.println("Memasak hanya bisa dilakukan di dalam rumah.");
            return;
        }

        Location loc = player.getLocation_infarm();
        if (!farm.getFarmMap().isInsideHouse(loc)) {
            System.out.println("Kamu harus berada di dalam rumah untuk memasak.");
            return;
        }

        if (player.getEnergy() < energyCost) {
            System.out.println("Energi tidak cukup untuk memasak.");
            return;
        }

        Item fuelItem = null;
        int fuelCapacity = 0;
        if (fuelType.equalsIgnoreCase("Firewood")) {
            fuelItem = new Misc("Firewood");
            fuelCapacity = 1;
        } else if (fuelType.equalsIgnoreCase("Coal")) {
            fuelItem = new Misc("Coal");
            fuelCapacity = 2;
        } else {
            System.out.println("Jenis fuel tidak dikenal.");
            return;
        }

        if (!player.haveitem(fuelItem)) {
            System.out.println("Fuel tidak tersedia di inventory.");
            return;
        }

        for (Map.Entry<Item, Integer> entry : recipe.getIngredients().entrySet()) {
            if (!player.getInventory().getItemsMap().containsKey(entry.getKey()) || player.getInventory().getItemsMap().get(entry.getKey()) < entry.getValue()) {
                System.out.println("Bahan tidak cukup untuk memasak " + recipe.getName());
                return;
            }
        }

        for (Map.Entry<Item, Integer> entry : recipe.getIngredients().entrySet()) {
            player.removefromInventory(entry.getKey(), entry.getValue());
        }
        player.removefromInventory(fuelItem, 1);

        player.setEnergy(player.getEnergy() - energyCost);
        System.out.println("Memasak " + recipe.getName() + " selama 1 jam...");
        farm.getTime().addMinutes(timeCostInMinute);

        player.addInventory(recipe.getResultFood());
        System.out.println("Selesai! " + recipe.getName() + " telah masuk ke inventory.");
    }
}
