package Action;
import GameCalendar.Model.GameTime;
import Player.Player;

public class ShowTime extends Action {
    public ShowTime() {
        super(0, 0);
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        clearScreen();
        System.out.println("=== WAKTU DAN MUSIM ===");
        System.out.printf("Waktu: %s | Hari: %d | Musim: %s | Cuaca: %s\n",
            gameTime.getCurrTime(), gameTime.getCurrDay(), gameTime.getCurrSeason(), gameTime.getCurrWeather());
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
    private void clearScreen() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            try {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } catch (Exception e) {
                for (int i = 0; i < 50; ++i) System.out.println();
            }
        } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}