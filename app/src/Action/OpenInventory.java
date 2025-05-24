package Action;
import GameCalendar.Model.GameTime;
import Player.Player;


public class OpenInventory extends Action {

    public OpenInventory() {
        super(0, 0); // Tidak konsumsi waktu/energi
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        System.out.println("=== INVENTORY ===");
        System.out.println(player.getInventory().printInventory());
    }
}