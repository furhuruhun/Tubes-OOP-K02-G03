package Action;

public class Fishing extends Action {
    public Fishing() {
        super(5, 15);
    }

    @Override
    public void perform(Player player) {
        Farm farm = player.getFarmname();
        String location = player.getLocation_inworld();

        if (location.equals("Farm")) {
            Location playerLoc = player.getLocation_infarm();
            if (!farm.getFarmMap().isNearPond(playerLoc)) {
                System.out.println("Kamu harus berada di dekat Pond untuk memancing di Farm.");
                return;
            }
        } else if (!(location.equals("Mountain Lake") || location.equals("Forest River") || location.equals("Ocean"))) {
            System.out.println("Kamu tidak berada di lokasi yang tepat.");
            return;
        }

        if (player.getEnergy() < energyCost) {
            System.out.println("Energi tidak cukup untuk memancing.");
            return;
        }

        player.setEnergy(player.getEnergy() - energyCost);
        farm.getTime().addMinutes(timeCostInMinute);

        String season = farm.getSeason().getCurrentSeason();
        int currentTime = farm.getTime().getCurrentTimeInMinutes();
        String weather = farm.getWeather().getCurrentWeather();

        List<Fish> possibleFish = FishDatabase.getFishFor(location, season, currentTime, weather);
        if (possibleFish.isEmpty()) {
            System.out.println("Tidak ada ikan yang bisa ditangkap di lokasi ini saat ini.");
            return;
        }

        Fish selectedFish = possibleFish.get(new Random().nextInt(possibleFish.size()));
        System.out.println("Kamu mencoba menangkap ikan " + selectedFish.getName() + "...");

        int target = 0, maxAttempts = 0;
        switch (selectedFish.getFishType()) {
            case "common":
                target = new Random().nextInt(10) + 1;
                maxAttempts = 10;
                break;
            case "regular":
                target = new Random().nextInt(100) + 1;
                maxAttempts = 10;
                break;
            case "legendary":
                target = new Random().nextInt(500) + 1;
                maxAttempts = 7;
                break;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Tebak angka untuk menangkap ikan dalam " + maxAttempts + " percobaan:");
        for (int i = 1; i <= maxAttempts; i++) {
            System.out.print("Percobaan ke " + i + ": ");
            int guess = scanner.nextInt();
            if (guess == target) {
                System.out.println("Berhasil! Kamu mendapatkan ikan " + selectedFish.getName() + "!");
                player.addInventory(selectedFish);
                return;
            }
            else if (guess < target) {
                System.out.println("Terlalu kecil.");
            }
            else {
                System.out.println("Terlalu besar.");
            }
        }

        System.out.println("Sayang sekali, kamu tidak berhasil menangkap ikan.");
    }
}