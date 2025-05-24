package Tester;
import Action.Chatting;
import GameCalendar.Model.*;
import Player.*;
import inventory.Inventory;
import items.Items;
import farm.World


public class MainChat {
    public static void main(String[] args) {
    Inventory inventory = new Inventory();
    Farm farm = new Farm("Spakbor Farm");
    Items apple = new Items("Apple");

    // Buat NPC
    NPC npc = new NPC("Anna", 50, apple, null, null, "Single", "Farm", 0);

    // Buat Player
    Player player = new Player("Budi", "Laki-laki", farm, null, inventory);
    player.setLocation_inworld("Farm"); // Samakan lokasi dengan NPC agar bisa ngobrol

    // Buat GameTime
    GameTime gameTime = new GameTime();

    // Lakukan aksi Chatting
    Chatting chatting = new Chatting(npc);
    chatting.perform(player, gameTime);    
    }
    
}
