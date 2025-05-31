package Action;
import java.util.Scanner;

import GameCalendar.Model.GameTime;
import Player.Player;


public class OpenInventory extends Action {

    public OpenInventory() {
        super(0, 0); // Tidak konsumsi waktu/energi
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        clearScreen();
        System.out.println("=== INVENTORY ===");
        System.out.println(player.getInventory().printInventory());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Tekan 'enter' untuk kembali ke menu utama.");
        scanner.nextLine(); // Tunggu input dari pengguna
        clearScreen();
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