// Game.java
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import Action.Action;
import Action.Chatting;
import Action.Cooking;
import Action.Eating;
import Action.Fishing;
import Action.Gifting;
import Action.Harvesting;
import Action.Marry;
import Action.Moving;
import Action.OpenInventory;
import Action.Planting;
import Action.Proposing;
import Action.Recover;
import Action.Selling;
import Action.ShowLocation;
import Action.ShowTime;
import Action.Sleeping;
import Action.Tilling;
import Action.Visiting;
import Action.Watching;
import Action.Watering;
import GameCalendar.Model.GameTime;
import GameCalendar.Model.Season;
import GameCalendar.Model.Weather;
import GameCalendar.Observer.TimeObserver;
import GameCalendar.View.TimeView;
import House.House;
import Juday.MainJuday;
import Player.Location;
import Player.NPC;
import Player.Player;
import Pond.Pond;
import SaveLoad.FarmData;
import SaveLoad.GameTimeData;
import SaveLoad.LocationSaveData;
import SaveLoad.NpcSaveData;
import SaveLoad.PlayerData;
import SaveLoad.SaveGameData;
import SaveLoad.SeedSaveData;
import SaveLoad.StatisticsData;
import SaveLoad.TileSaveData;
import ShippingBin.ShippingBin;
import farm.Farm;
import farm.FarmMap.FarmMap;
import farm.FarmMap.Tile;
import farm.World.WorldMap;
import inventory.Inventory;
import items.Crops;
import items.Equipment;
import items.Fish;
import items.Food;
import items.Items;
import items.Misc;
import items.Recipe;
import items.Seeds;

public class Game implements TimeObserver{
    private Player player;
    private Farm farm;
    private GameTime gameTime;
    private TimeView timeView;
    private WorldMap worldMap;
    private boolean isRunning;
    private Scanner scanner;
    private HashMap<String, Action> actionMap;
    private List<NPC> npcs;
    private List<Recipe> recipes;
    private List<Items> allGameItems;
    private Gson gson = new Gson();
    private static final String SAVE_FILE_PATH = "savegame.json";
    private Moving movingAction;

    public int totalIncome = 0;
    public int totalExpenditure = 0;
    public int totalDaysPlayed = 0;
    public int totalCropsHarvested = 0;
    public int totalFishCaught = 0;
    public int commonFishCaught = 0;
    public int regularFishCaught = 0;
    public int legendaryFishCaught = 0;

    private HashMap<NPC, Integer> chatFrequency;
    private HashMap<NPC, Integer> giftFrequency;
    private HashMap<NPC, Integer> visitFrequency;
    private Location temp;

    private boolean goldMilestoneReached = false;
    private boolean marriageMilestoneReached = false;

    private Timer gameTimer; // Timer untuk aliran waktu pasif
    private final List<String> storeRecipeNames = Arrays.asList("Fish n' Chips", "Fish Sandwich");
    private static final int DEFAULT_RECIPE_PRICE_IN_STORE = 200;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        this.actionMap = new HashMap<>();
        this.npcs = new ArrayList<>();
        this.recipes = new ArrayList<>();
        this.allGameItems = new ArrayList<>();
        this.chatFrequency = new HashMap<>();
        this.giftFrequency = new HashMap<>();
        this.visitFrequency = new HashMap<>();

        this.gameTime = new GameTime(); //
        this.gameTime.registerTimeObserver(this);

        this.timeView = new TimeView();
        this.worldMap = new WorldMap();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        // gameTimer akan null secara default


        initializeItemsAndRecipes();
        initializeNPCs();
        initializeActions();
    }

    @Override
    public synchronized void onTimeUpdate(GameTime currentTime) {
        // if (player != null && player.getEnergy() <= -20 && isRunning) {
        //     System.out.println("ALERT OBSERVER: Energi habis karena waktu berjalan pasif!");}
    }

    @Override
    public synchronized void onTwoAM(GameTime currentTime) {
        // Metode ini dipanggil secara spesifik saat jam 2 AM.
        if (!isRunning) return; // Jika game sudah dalam proses berhenti, jangan lakukan apa-apa.

        System.out.println("ALERT OBSERVER: Sudah jam 2 PAGI! Waktunya tidur atau game berakhir.");
        if (player != null && actionMap != null && actionMap.containsKey("sleeping")) {
            handleAutoSleepOrTermination();
        } else {
            System.out.println("Game akan dihentikan karena sudah jam 2 pagi dan tidak bisa tidur otomatis.");
            this.isRunning = false; // Fallback jika tidak bisa tidur
        }
    }
    private void handleAutoSleepOrTermination() {
        if (!this.isRunning) return; // Sudah dalam proses terminasi

        // Cek apakah pemain masih ada (belum null karena kondisi lain)
        if (player == null) {
            System.out.println("Pemain tidak ditemukan, game akan dihentikan.");
            this.isRunning = false;
            return;
        }

        if (player.getEnergy() <= -20) {
            System.out.println("Energi habis! Kamu pingsan dan otomatis tidur.");
        } else {
            System.out.println("Waktu sudah menunjukkan pukul 02:00. Kamu otomatis tidur.");
        }
        FarmMap farmMap = player.getFarmname().getFarmMap();
        Location playerLocationInFarm = player.getLocation_infarm();
        String playerLocationInWorld = player.getLocation_inworld();
        if(player.getEnergy() < 10 && player.getEnergy() > -20){
            player.setEnergy(player.getEnergy() + 50);
        }
        else if(player.getEnergy() == 0){
            player.setEnergy(player.getEnergy() + 10);
        }
        else{
            player.setEnergy(100);
        }

        gameTime.timeSkip(6);
        Location oldPlayerLocationOnFarm = null;
        if(playerLocationInWorld.equalsIgnoreCase("Farm") && playerLocationInFarm != null){
            oldPlayerLocationOnFarm = playerLocationInFarm;
        }

        player.setLocation_inworld("Farm");
        Location houseLoc = player.getFarmname().getFarmMap().getHouse().getLocation();

        if(houseLoc != null){
            int bangunX = houseLoc.getX() + (House.DEFAULT_WIDTH / 2);
            int bangunY = houseLoc.getY() + House.DEFAULT_HEIGHT;

            if (bangunY >= 32){
                bangunY = 31;
            }

            if (bangunX >= 32){
                bangunX = 31;
            }

            Location lokasibangun = new Location(bangunX, bangunY);
            if(oldPlayerLocationOnFarm != null && farmMap.getTile(oldPlayerLocationOnFarm) != null){
                farmMap.getTile(oldPlayerLocationOnFarm).setSymbol('.');
            }
            
            player.setLocation_infarm(lokasibangun);
            
            if(farmMap.getTile(lokasibangun) != null){
                farmMap.getTile(lokasibangun).setSymbol('p');
            }
        }
        else{
            player.setLocation_infarm(new Location(10, 10));
             if(farmMap.getTile(new Location(10,10)) != null){
                 farmMap.getTile(new Location(10,10)).setSymbol('p');
             }
        }
        System.out.println("Selamat pagi! Hari baru telah tiba.");
        System.out.println("Energi kamu telah dipulihkan.");
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public Items findItemByName(String name) {
        if (name == null) return null;
        for (Items item : allGameItems) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        if (name.equalsIgnoreCase("AnyFishPlaceholder")) return new Items("AnyFishPlaceholder");
        if (name.equalsIgnoreCase("LegendFishPlaceholder")) return new Items("LegendFishPlaceholder");
        return null;
    }

        public Recipe findRecipeByName(String name) {
        if (name == null || recipes == null) return null;
        for (Recipe recipe : recipes) {
            if (recipe != null && recipe.getName() != null && recipe.getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

    public void unlockRecipe(String recipeName) {
        Recipe recipeToUnlock = findRecipeByName(recipeName);
        if (recipeToUnlock != null && !recipeToUnlock.getStatus()) {
            recipeToUnlock.setStatus(true);
            System.out.println("RESEP BARU TERBUKA: " + recipeName + "!");
        }
    }

    public void run() {
        while (isRunning) {
            showMainMenu();
            String choice = scanner.nextLine().trim();
            handleMainMenu(choice);
            if (player != null && !isRunning) {
                 checkEndGameConditions(true);
            } else if (player != null && isRunning) {
                 // Kondisi ini mungkin perlu penyesuaian
                 // checkEndGameConditions(false);
            }
        }
        stopPassiveTimeFlow(); // Pastikan timer berhenti saat aplikasi ditutup
        if (this.gameTime != null) {
            this.gameTime.removeTimeObserver(this); // Unregister ketika berakhir
        }
        System.out.println("Terima kasih telah bermain Spakbor Hills!");
    }

    private void showMainMenu() {
        System.out.println("\n==== SPAKBOR HILLS ====");
        System.out.println("1. New Game");
        System.out.println("2. Help");
        System.out.println("3. View Player Info");
        System.out.println("4. Statistics");
        System.out.println("5. Actions (Lanjutkan Permainan)");
        System.out.println("6. Credits");
        System.out.println("7. Exit");
        System.out.println("8. Save Game"); 
        System.out.println("9. Load Game");
        System.out.print(">> ");
    }

    private void saveGame() {
        if (player == null) {
            System.out.println("Tidak ada game yang sedang berjalan untuk disimpan.");
            return;
        }

        SaveGameData saveFile = new SaveGameData();

        // 1. Player Data
        saveFile.playerData = new PlayerData();
        saveFile.playerData.name = player.getName();
        saveFile.playerData.gender = player.getGender();
        saveFile.playerData.energy = player.getEnergy();
        saveFile.playerData.maxEnergy = 100; // Asumsi dari kode Anda
        saveFile.playerData.farmName = farm.getName();
        saveFile.playerData.partnerName = (player.getPartner() != null) ? player.getPartner().getName() : null;
        saveFile.playerData.gold = player.getGold();
        saveFile.playerData.inventory = new HashMap<>();
        for (Map.Entry<Items, Integer> entry : player.getInventory().getItemsMap().entrySet()) {
            saveFile.playerData.inventory.put(entry.getKey().getName(), entry.getValue());
        }
        saveFile.playerData.locationInFarm = player.getLocation_infarm() != null ? new LocationSaveData(player.getLocation_infarm().getX(), player.getLocation_infarm().getY()) : null;
        saveFile.playerData.locationInWorld = player.getLocation_inworld();


        // 2. Farm Data
        saveFile.farmData = new FarmData();
        saveFile.farmData.farmName = farm.getName();
        if (farm.getFarmMap().getHouse() != null && farm.getFarmMap().getHouse().getLocation() != null) {
            saveFile.farmData.houseLocation = new LocationSaveData(farm.getFarmMap().getHouse().getLocation());
        }
        if (farm.getFarmMap().getPond() != null && farm.getFarmMap().getPond().getLocation() != null) { // Pond ada di FarmMap
            saveFile.farmData.pondLocation = new LocationSaveData(farm.getFarmMap().getPond().getLocation());
        }
        if (farm.getFarmMap().getShippingBin() != null && farm.getFarmMap().getShippingBin().getLocation() != null) { // Jika ShippingBin punya getLocation()
            saveFile.farmData.shippingBinLocation = new LocationSaveData(farm.getFarmMap().getShippingBin().getLocation());
        }


        saveFile.farmData.tiles = new ArrayList<>();
        if (farm.getFarmMap() != null && farm.getFarmMap().getTiles() != null) {
            for (Map.Entry<Location, Tile> entry : farm.getFarmMap().getTiles().entrySet()) {
                Location loc = entry.getKey();
                Tile tile = entry.getValue();
                TileSaveData tileSave = new TileSaveData();
                tileSave.x = loc.getX();
                tileSave.y = loc.getY();
                // Simpan simbol ASLI tile, bukan 'p' jika pemain di atasnya
                if (player.getLocation_inworld().equals("Farm") && player.getLocation_infarm() != null && player.getLocation_infarm().equals(loc)) {
                    // Aksi Moving menyimpan simbol asli di 'temp'
                    tileSave.symbol = (movingAction != null) ? movingAction.getTemp() : tile.getSymbol(); // fallback ke simbol tile jika movingAction null
                } else {
                    tileSave.symbol = tile.getSymbol();
                }

                if (tile.isPlanted() && tile.getPlantedSeed() != null) {
                    tileSave.plantedSeed = new SeedSaveData(tile.getPlantedSeed().getName(), tile.getPlantedSeed().getStartday());
                } else {
                    tileSave.plantedSeed = null;
                }
                tileSave.isWatered = tile.isWatered();
                saveFile.farmData.tiles.add(tileSave);
            }
        }
        saveFile.farmData.shippingBinContents = new HashMap<>();
        if (farm.getFarmMap() != null && farm.getFarmMap().getShippingBin() != null) { // Periksa null
            for (Map.Entry<Items, Integer> entry : farm.getFarmMap().getShippingBin().getBin().entrySet()) { // Asumsi getBin() mengembalikan Map
                saveFile.farmData.shippingBinContents.put(entry.getKey().getName(), entry.getValue());
            }
        }


        // 3. GameTime Data
        saveFile.gameTimeData = new GameTimeData();
        saveFile.gameTimeData.hour = gameTime.getCurrTime().getHour();
        saveFile.gameTimeData.minute = gameTime.getCurrTime().getMinute();
        saveFile.gameTimeData.currentDay = gameTime.getCurrDay();
        saveFile.gameTimeData.currentSeason = gameTime.getCurrSeason().name();
        saveFile.gameTimeData.currentWeather = gameTime.getCurrWeather().name();
        saveFile.gameTimeData.rainyDaysThisSeason = gameTime.getRainyDaysThisSeason();
        saveFile.gameTimeData.isPaused = gameTime.isPaused();

        // 4. NPCs Data
        saveFile.npcsData = new ArrayList<>();
        for (NPC npc : npcs) {
            NpcSaveData npcSave = new NpcSaveData();
            npcSave.name = npc.getName();
            npcSave.heartpoints = npc.getHeartpoints();
            npcSave.relationshipStatus = npc.getRelationshipStatus();
            npcSave.dayEngaged = npc.getDayEngaged();
            saveFile.npcsData.add(npcSave);
        }

        // 5. Recipe Status
        saveFile.recipesUnlocked = new HashMap<>();
        for (Recipe recipe : recipes) {
            saveFile.recipesUnlocked.put(recipe.getName(), recipe.getStatus());
        }

        // 6. Statistics Data
        saveFile.statisticsData = new StatisticsData();
        saveFile.statisticsData.totalIncome = this.totalIncome;
        saveFile.statisticsData.totalExpenditure = this.totalExpenditure;
        saveFile.statisticsData.totalDaysPlayed = this.totalDaysPlayed;
        saveFile.statisticsData.totalCropsHarvested = this.totalCropsHarvested;
        saveFile.statisticsData.totalFishCaught = this.totalFishCaught;
        saveFile.statisticsData.commonFishCaught = this.commonFishCaught;
        saveFile.statisticsData.regularFishCaught = this.regularFishCaught;
        saveFile.statisticsData.legendaryFishCaught = this.legendaryFishCaught;
        saveFile.statisticsData.chatFrequency = new HashMap<>();
        for(Map.Entry<NPC, Integer> entry : chatFrequency.entrySet()){
            saveFile.statisticsData.chatFrequency.put(entry.getKey().getName(), entry.getValue());
        }
        saveFile.statisticsData.giftFrequency = new HashMap<>();
         for(Map.Entry<NPC, Integer> entry : giftFrequency.entrySet()){
            saveFile.statisticsData.giftFrequency.put(entry.getKey().getName(), entry.getValue());
        }
        saveFile.statisticsData.visitFrequency = new HashMap<>();
        for(Map.Entry<NPC, Integer> entry : visitFrequency.entrySet()){
            saveFile.statisticsData.visitFrequency.put(entry.getKey().getName(), entry.getValue());
        }
        saveFile.statisticsData.goldMilestoneReached = this.goldMilestoneReached;
        saveFile.statisticsData.marriageMilestoneReached = this.marriageMilestoneReached;


        try (FileWriter writer = new FileWriter(SAVE_FILE_PATH)) {
            gson.toJson(saveFile, writer);
            System.out.println("Game berhasil disimpan ke " + SAVE_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadGame() {
        try (FileReader reader = new FileReader(SAVE_FILE_PATH)) {
            Type saveDataType = new TypeToken<SaveGameData>(){}.getType();
            SaveGameData saveFile = gson.fromJson(reader, saveDataType);

            if (saveFile == null) {
                System.out.println("File save tidak ditemukan atau rusak.");
                return;
            }
            
            stopPassiveTimeFlow(); // Hentikan timer game saat ini sebelum memuat

            // 1. Restore GameTime
            this.gameTime = new GameTime(); // Buat instance baru
            this.gameTime.setCurrTime(LocalTime.of(saveFile.gameTimeData.hour, saveFile.gameTimeData.minute));
            this.gameTime.setCurrDay(saveFile.gameTimeData.currentDay);
            this.gameTime.setCurrSeason(Season.valueOf(saveFile.gameTimeData.currentSeason));
            this.gameTime.setCurrWeather(Weather.valueOf(saveFile.gameTimeData.currentWeather));
            this.gameTime.setRainyDaysThisSeason(saveFile.gameTimeData.rainyDaysThisSeason);
            this.gameTime.setPaused(saveFile.gameTimeData.isPaused);


            // 2. Restore Farm and FarmMap
            House loadedHouse = new House();
            Pond loadedPond = new Pond();
            ShippingBin loadedShippingBin = new ShippingBin();

            if (saveFile.farmData.houseLocation != null) loadedHouse.setLocation(saveFile.farmData.houseLocation.toPlayerLocation());
            if (saveFile.farmData.pondLocation != null) loadedPond.setLocation(saveFile.farmData.pondLocation.toPlayerLocation());
            // Lokasi ShippingBin mungkin perlu di-set jika disimpan, atau dihitung ulang.

            FarmMap loadedFarmMap = new FarmMap(loadedHouse, loadedPond, loadedShippingBin);
            loadedFarmMap.generateMap(); // Generate empty map first

            // Restore tiles
            if (saveFile.farmData.tiles != null) {
                for (TileSaveData tileSave : saveFile.farmData.tiles) {
                    Location loc = new Location(tileSave.x, tileSave.y);
                    Tile tile = loadedFarmMap.getTile(loc);
                    if (tile != null) {
                        tile.setSymbol(tileSave.symbol); // Simbol asli sudah disimpan
                        tile.setWatered(tileSave.isWatered);
                        if (tileSave.plantedSeed != null) {
                            Items seedItem = findItemByName(tileSave.plantedSeed.seedName);
                            if (seedItem instanceof Seeds) {
                                Seeds actualSeed = (Seeds) seedItem;
                                // Buat instance baru dari seed yang ditanam atau clone, jangan pakai objek yang sama dari allGameItems
                                // karena startDay-nya bisa berbeda per tile.
                                Seeds planted = new Seeds(actualSeed.getName(), actualSeed.getSeason(), actualSeed.getDaystoharvest(), actualSeed.getCrops(), actualSeed.getHargaJual(), actualSeed.getHargaBeli(), this.gameTime);
                                planted.setStartday(tileSave.plantedSeed.startDay);
                                tile.setPlantedSeed(planted);
                            }
                        }
                    }
                }
            }
            // Place house, pond, bin based on saved locations (setelah tiles di-restore simbolnya)
             if (loadedHouse.getLocation() != null) {
                loadedFarmMap.placeStructure(loadedHouse.getLocation().getX(), loadedHouse.getLocation().getY(), House.DEFAULT_WIDTH, House.DEFAULT_HEIGHT, 'h');
            }
            if (loadedPond.getLocation() != null) {
                loadedFarmMap.placeStructure(loadedPond.getLocation().getX(), loadedPond.getLocation().getY(), 4, 3, 'o'); // Asumsi ukuran pond
            }
            // Tempatkan shipping bin, mungkin perlu logika yang sama dengan placeRandomShippingBin tapi dengan lokasi dari save file jika ada
            // Jika tidak, tempatkan secara acak atau relatif terhadap rumah.
            // Untuk sementara, kita akan coba panggil ulang logika penempatan acak (ini mungkin menimpa simbol tile lain jika tidak hati-hati)
            // Cara yang lebih baik adalah menyimpan lokasi pastinya.
            // loadedFarmMap.placeRandomShippingBin(); // Ini akan mencari spot baru. Jika lokasi disimpan, gunakan itu.


            this.farm = new Farm(saveFile.farmData.farmName, this.gameTime, loadedFarmMap);

            // Restore ShippingBin contents
            if(saveFile.farmData.shippingBinContents != null){
                for(Map.Entry<String, Integer> entry : saveFile.farmData.shippingBinContents.entrySet()){
                    Items item = findItemByName(entry.getKey());
                    if(item != null){
                        // Perlu cara untuk menambahkan langsung ke map di ShippingBin, atau metode addItem tanpa cek inventory player
                        // Untuk sementara, kita asumsikan ada cara langsung
                        this.farm.getFarmMap().getShippingBin().getBin().put(item, entry.getValue());
                    }
                }
            }


            // 3. Restore Player
            Inventory loadedInventory = new Inventory();
            if (saveFile.playerData.inventory != null) {
                for (Map.Entry<String, Integer> entry : saveFile.playerData.inventory.entrySet()) {
                    Items item = findItemByName(entry.getKey());
                    if (item != null) {
                        loadedInventory.addItem(item, entry.getValue());
                    } else {
                        System.err.println("Peringatan saat load: Item '" + entry.getKey() + "' tidak ditemukan dalam definisi game.");
                    }
                }
            }
            this.player = new Player(saveFile.playerData.name, saveFile.playerData.gender, this.farm, null, loadedInventory);
            player.setEnergy(saveFile.playerData.energy);
            // player.setMaxEnergy() jika ada setter
            player.setGold(saveFile.playerData.gold);
            if (saveFile.playerData.locationInFarm != null) {
                 player.setLocation_infarm(new Location(saveFile.playerData.locationInFarm.x, saveFile.playerData.locationInFarm.y));
                 // Set simbol 'p' di farm map dan 'temp' di Moving action
                 if (player.getLocation_inworld().equals("Farm")) {
                     Tile playerCurrentTile = this.farm.getFarmMap().getTile(player.getLocation_infarm());
                     if (playerCurrentTile != null) {
                         if (this.movingAction != null) {
                             this.movingAction.setTemp(playerCurrentTile.getSymbol()); // Simpan simbol asli sebelum ditimpa 'p'
                         }
                         playerCurrentTile.setSymbol('p');
                     }
                 }
            } else {
                player.setLocation_infarm(null);
            }
            player.setLocation_inworld(saveFile.playerData.locationInWorld);


            // 4. Restore NPCs
            initializeNPCs(); // Re-init NPC list (untuk loved/liked items yang statis)
            if (saveFile.npcsData != null) {
                for (NpcSaveData npcSave : saveFile.npcsData) {
                    NPC npc = findNpcByName(npcSave.name); // Anda perlu method ini
                    if (npc != null) {
                        npc.setHeartpoints(npcSave.heartpoints);
                        npc.setRelationshipStatus(npcSave.relationshipStatus);
                        npc.setDayEngaged(npcSave.dayEngaged);
                        if (npc.getName().equalsIgnoreCase(saveFile.playerData.partnerName)) {
                            player.setPartner(npc);
                        }
                    }
                }
            }

            // 5. Restore Recipe Status
            initializeItemsAndRecipes(); // Re-init resep (untuk daftar resep dan bahan dasarnya)
            if (saveFile.recipesUnlocked != null) {
                for (Map.Entry<String, Boolean> entry : saveFile.recipesUnlocked.entrySet()) {
                    Recipe recipe = findRecipeByName(entry.getKey()); // Anda perlu method ini
                    if (recipe != null) {
                        recipe.setStatus(entry.getValue());
                    }
                }
            }

            // 6. Restore Statistics
            if (saveFile.statisticsData != null) {
                this.totalIncome = saveFile.statisticsData.totalIncome;
                this.totalExpenditure = saveFile.statisticsData.totalExpenditure;
                this.totalDaysPlayed = saveFile.statisticsData.totalDaysPlayed;
                this.totalCropsHarvested = saveFile.statisticsData.totalCropsHarvested;
                this.totalFishCaught = saveFile.statisticsData.totalFishCaught;
                this.commonFishCaught = saveFile.statisticsData.commonFishCaught;
                this.regularFishCaught = saveFile.statisticsData.regularFishCaught;
                this.legendaryFishCaught = saveFile.statisticsData.legendaryFishCaught;

                this.chatFrequency.clear();
                if (saveFile.statisticsData.chatFrequency != null) {
                    for(Map.Entry<String, Integer> entry : saveFile.statisticsData.chatFrequency.entrySet()){
                        NPC npc = findNpcByName(entry.getKey());
                        if(npc != null) this.chatFrequency.put(npc, entry.getValue());
                    }
                }
                this.giftFrequency.clear();
                if (saveFile.statisticsData.giftFrequency != null) {
                     for(Map.Entry<String, Integer> entry : saveFile.statisticsData.giftFrequency.entrySet()){
                        NPC npc = findNpcByName(entry.getKey());
                        if(npc != null) this.giftFrequency.put(npc, entry.getValue());
                     }
                }

                this.visitFrequency.clear();
                if (saveFile.statisticsData.visitFrequency != null) {
                    for(Map.Entry<String, Integer> entry : saveFile.statisticsData.visitFrequency.entrySet()){
                        NPC npc = findNpcByName(entry.getKey());
                        if(npc != null) this.visitFrequency.put(npc, entry.getValue());
                    }
                }
                this.goldMilestoneReached = saveFile.statisticsData.goldMilestoneReached;
                this.marriageMilestoneReached = saveFile.statisticsData.marriageMilestoneReached;
            }


            System.out.println("Game berhasil dimuat dari " + SAVE_FILE_PATH);
            // if (!this.gameTime.isPaused()) { // Jika game tidak di-pause di save file
            //      startPassiveTimeFlow(); // Mulai kembali aliran waktu jika tidak sedang di-pause
            // }
            // Lanjutkan ke game loop
            inGameLoop();

        } catch (IOException e) {
            System.err.println("Gagal memuat game: " + e.getMessage());
        } catch (Exception e) { // Tangkap kesalahan lain seperti parsing JSON atau NullPointer
            System.err.println("Terjadi kesalahan saat memuat game: " + e.getMessage());
            e.printStackTrace();
            this.player = null; // Reset player jika load gagal
        }
    }

    private NPC findNpcByName(String name) {
        if (name == null) return null;
        for (NPC npc : npcs) {
            if (npc.getName().equalsIgnoreCase(name)) {
                return npc;
            }
        }
        return null;
    }

    private void handleMainMenu(String input) {
        switch (input) {
            case "1":
                // stopPassiveTimeFlow(); // Hentikan timer jika ada dari sesi game sebelumnya
                startNewGame();
                break;
            case "2": showHelp(); break;
            case "3": viewPlayerInfo(); break;
            case "4": showStatistics(); break;
            case "5":
                if (player != null) {
                    System.out.println("Melanjutkan permainan...");
                    inGameLoop();
                } else {
                    System.out.println("Mulai permainan baru terlebih dahulu (New Game).");
                }
                break;
            case "6": showCredits(); break;
            case "7":
                isRunning = false;
                // stopPassiveTimeFlow() akan dipanggil di akhir run()
                break;
            case "8": saveGame(); break;
            case "9": loadGame(); break;

            default: System.out.println("Pilihan tidak valid.");
        }
    }

    public void startNewGame() {
        System.out.println("\n=== MULAI GAME BARU ===");
        System.out.print("Masukkan nama pemain: ");
        String name = scanner.nextLine();
        while(name.equals("")|| name == null){
            System.out.println("Nama tidak Valid");
            System.out.print("Masukkan nama pemain: ");
            name = scanner.nextLine();
        }
        String gender;
        while (true) {
            System.out.print("Masukkan gender (Laki-laki/Perempuan): ");
            gender = scanner.nextLine();
            if (gender.equalsIgnoreCase("Laki-laki") || gender.equalsIgnoreCase("Perempuan")) break;
            System.out.println("Gender tidak valid. Harap masukkan 'Laki-laki' atau 'Perempuan'.");
        }
        System.out.print("Masukkan nama Farm: ");
        String farmNameInput = scanner.nextLine();
        while(farmNameInput == null || farmNameInput.equals("")){
            System.out.println("Nama farm tidak Valid");
            System.out.print("Masukkan nama Farm: ");
            farmNameInput = scanner.nextLine();
        }

        //unregist dari yang lama ke yang baru
        if (this.gameTime != null) {
            this.gameTime.removeTimeObserver(this);
        }
        this.gameTime = new GameTime(); // Reset objek gameTime untuk game baru
        this.gameTime.registerTimeObserver(this);

        House houseInstance = new House();
        Pond pond = new Pond();
        ShippingBin shippingBin = new ShippingBin();
        FarmMap farmMap = new FarmMap(houseInstance, pond, shippingBin);

        farmMap.generateMap();
        farmMap.placeRandomHouse();
        farmMap.placeRandomPond();
        farmMap.placeRandomShippingBin();

        this.farm = new Farm(farmNameInput, gameTime, farmMap);
        Location houseLoc = farm.getFarmMap().getHouse().getLocation();

        Inventory inventory = new Inventory();
        Items parsnipSeedsItem = findItemByName("Parsnip Seeds");
        if (parsnipSeedsItem != null) inventory.addItem(parsnipSeedsItem, 15); else System.err.println("Error: Parsnip Seeds item not found for initial inventory.");
        Items hoeItem = findItemByName("Hoe");
        if (hoeItem != null) inventory.addItem(hoeItem, 1); else System.err.println("Error: Hoe item not found for initial inventory.");
        Items wateringCanItem = findItemByName("Watering Can");
        if (wateringCanItem != null) inventory.addItem(wateringCanItem, 1); else System.err.println("Error: Watering Can item not found for initial inventory.");
        Items pickaxeItem = findItemByName("Pickaxe");
        if (pickaxeItem != null) inventory.addItem(pickaxeItem, 1); else System.err.println("Error: Pickaxe item not found for initial inventory.");
        Items fishingRodItem = findItemByName("Fishing Rod");
        if (fishingRodItem != null) inventory.addItem(fishingRodItem, 1); else System.err.println("Error: Fishing Rod item not found for initial inventory.");

        this.player = new Player(name, gender, farm, null, inventory);
        this.player.setGold(0); this.player.setEnergy(100); this.player.setLocation_inworld("Farm");

        Location playerStartLocation = new Location(10,10);
        if (houseLoc != null) {
            playerStartLocation = new Location(houseLoc.getX() + houseInstance.getWidth()/2, houseLoc.getY() + houseInstance.getHeight());
            if (playerStartLocation.getX() < 0 || playerStartLocation.getX() >= 32 || playerStartLocation.getY() < 0 || playerStartLocation.getY() >= 32 ||
                farm.getFarmMap().getTile(playerStartLocation) == null || farm.getFarmMap().getTile(playerStartLocation).getSymbol() != '.') {
                boolean foundStart = false;
                for (int r = 0; r < 32 && !foundStart; r++) {
                    for (int c = 0; c < 32; c++) {
                        Location potentialLoc = new Location(c,r);
                        if (farm.getFarmMap().getTile(potentialLoc) != null && farm.getFarmMap().getTile(potentialLoc).getSymbol() == '.') {
                            playerStartLocation = potentialLoc; foundStart = true; break;
                        }
                    }
                }
                if(!foundStart) playerStartLocation = new Location(0,0);
            }
        }
        this.player.setLocation_infarm(playerStartLocation);
        if(farm.getFarmMap().getTile(playerStartLocation) != null) farm.getFarmMap().getTile(playerStartLocation).setSymbol('p');
        else {
            System.err.println("Error: Tidak dapat menempatkan pemain di peta pada lokasi awal.");
            playerStartLocation = new Location(0,0); this.player.setLocation_infarm(playerStartLocation);
            if(farm.getFarmMap().getTile(playerStartLocation) != null) farm.getFarmMap().getTile(playerStartLocation).setSymbol('p');
        }

        resetStatistics();
        System.out.println("Game baru berhasil dimulai!");
        // gameTime.startTime(); // Dipanggil di dalam startPassiveTimeFlow jika perlu
        inGameLoop();
    }
    
    private void resetStatistics() { //
        totalIncome = 0; totalExpenditure = 0; totalDaysPlayed = 0;
        totalCropsHarvested = 0; totalFishCaught = 0; commonFishCaught = 0;
        regularFishCaught = 0; legendaryFishCaught = 0;
        chatFrequency.clear(); giftFrequency.clear(); visitFrequency.clear();
        for (NPC npc : npcs) {
            npc.setHeartpoints(0); npc.setRelationshipStatus("single"); npc.setDayEngaged(0);
            chatFrequency.put(npc, 0); giftFrequency.put(npc, 0); visitFrequency.put(npc, 0);
        }
        goldMilestoneReached = false; marriageMilestoneReached = false;
    }

    private void startPassiveTimeFlow() {
        if (gameTimer == null && player != null) {
            if (gameTime.isPaused()) { // Cek apakah gameTime sedang dijeda
                gameTime.startTime(); // Lanjutkan gameTime jika dijeda
            }
            gameTimer = new Timer(true); // true agar menjadi daemon thread
            gameTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (player != null && !gameTime.isPaused()) { // Pastikan game tidak dijeda
                        synchronized (gameTime) { // Sinkronisasi akses ke gameTime
                            gameTime.updateTime(); // Panggil metode updateTime dari GameTime
                        }
                    }
                }
            }, 0, 1000); // delay 0ms, period 1000ms (1 detik)
        }
    }

    private void stopPassiveTimeFlow() {
        if (gameTimer != null) {
            gameTimer.cancel();
            gameTimer = null;
            if (gameTime != null && !gameTime.isPaused()) { // Hanya panggil pauseTime jika tidak sedang dijeda
                gameTime.pauseTime(); // Atur status internal GameTime menjadi dijeda
            }
        }
    }

    public void performDailyPlantChecks() {
        if (farm == null || farm.getFarmMap() == null || farm.getFarmMap().getTiles() == null || gameTime == null) {
            System.err.println("Tidak bisa melakukan pengecekan tanaman harian, data game tidak lengkap.");
            return;
        }

        System.out.println("\n--- Pengecekan Kondisi Tanaman Pagi Hari ---");
        boolean isRainingToday = (gameTime.getCurrWeather() == Weather.RAINY);
        List<Location> tilesToClear = new ArrayList<>(); // Untuk menampung lokasi tanaman yang mati

        for (Map.Entry<Location, Tile> entry : farm.getFarmMap().getTiles().entrySet()) {
            Tile tile = entry.getValue();
            Location loc = entry.getKey();

            if (tile != null && tile.isPlanted()) {
                Seeds plantedSeed = tile.getPlantedSeed(); // Dapatkan info bibit untuk pesan

                if (isRainingToday) {
                    // Jika hari ini hujan, semua tanaman yang ada otomatis tersiram untuk hari ini.
                    tile.setWatered(true);
                } else {
                    // Jika tidak hujan hari ini:
                    // Cek status isWatered dari HARI SEBELUMNYA.
                    // tile.isWatered() pada titik ini masih menyimpan status dari akhir hari kemarin.
                    if (!tile.isWatered()) {
                        // Jika tile.isWatered() adalah 'false', berarti tanaman TIDAK disiram
                        // pada siklus hari sebelumnya DAN hari ini juga tidak hujan. Maka tanaman mati.
                        System.out.println("TANAMAN MATI! " + (plantedSeed != null ? plantedSeed.getName() : "Sebuah tanaman") +
                                           " di (" + loc.getX() + "," + loc.getY() + ") layu karena tidak disiram kemarin.");
                        tilesToClear.add(loc); // Tandai petak ini untuk dibersihkan tanamannya.
                    } else {
                        // Jika tile.isWatered() adalah 'true', berarti tanaman DISIRAM pada hari sebelumnya.
                        // Untuk HARI INI (yang tidak hujan), statusnya perlu direset menjadi 'false',
                        // menandakan ia membutuhkan air lagi HARI INI agar tidak mati besok.
                        tile.setWatered(false);
                    }
                }
            }
        }

        // Hapus semua tanaman yang mati dari peta
        for (Location locToClear : tilesToClear) {
            Tile tile = farm.getFarmMap().getTile(locToClear);
            if (tile != null) { 
                tile.clearTile(); // Method clearTile() akan menghapus bibit dan mereset simbol.
            }
        }

        if (isRainingToday) {
            System.out.println("Hujan pagi ini telah menyirami semua tanaman di ladang!");
        } else if (tilesToClear.isEmpty()){
            // Pesan ini bisa jadi terlalu verbose jika tidak ada tanaman mati, bisa di-skip
            // System.out.println("Semua tanaman yang perlu air kemarin sudah disiram atau masih bertahan.");
        }
        System.out.println("------------------------------------------");
    }

    private void inGameLoop() {
        boolean inGame = true;

         if (this.gameTime == null) {
            System.err.println("Kesalahan Kritis: GameTime null saat memulai inGameLoop. Membuat instance baru.");
            this.gameTime = new GameTime();
        }
        // Pastikan observer terdaftar (seharusnya sudah dari constructor/new/load)
        this.gameTime.removeTimeObserver(this); // Hapus dulu untuk menghindari duplikasi
        this.gameTime.registerTimeObserver(this);


        if (gameTime.isPaused() && player != null) { // Jika game di-load dalam keadaan pause
            // gameTime.startTime(); // Akan di-handle oleh startPassiveTimeFlow jika diperlukan
        }
        startPassiveTimeFlow(); // Mulai aliran waktu pasif saat memasuki loop game
        
        try {
            while (inGame && isRunning) {
                // Logika auto-sleep jika waktu mencapai 02:00 atau energi habis
                if (player.getEnergy() <= -20) {
                    System.out.println("ALERT OBSERVER: Energi pemain habis! Otomatis tidur untuk mengisi ulang energi.");
                    if (player != null && actionMap != null && actionMap.containsKey("sleeping")) {
                        handleAutoSleepOrTermination();
                    }
                    continue;
                }
                clearScreen();
                System.out.println("\n== APA YANG AKAN KAMU LAKUKAN? ==");
                timeView.displayAll(gameTime);
                System.out.println("Energi: " + player.getEnergy() + "/100");
                System.out.println("Gold: " + player.getGold() + "g");

                if (player.getLocation_inworld().equalsIgnoreCase("Farm") && player.getLocation_infarm() != null) {
                     farm.getFarmMap().printMap();
                     System.out.println("Lokasi: " + player.getLocation_inworld() + " (" + player.getLocation_infarm().getX() + "," + player.getLocation_infarm().getY() + ")");
                } else System.out.println("Lokasi: " + player.getLocation_inworld());

                // Menampilkan pilihan aksi
                System.out.println("Pilihan Aksi:\n1.  Moving         | 11. Proposing\n2.  Tilling        | 12. Marry\n3.  Planting       | 13. Watching TV\n4.  Watering       | 14. Visiting\n5.  Harvesting     | 15. Chatting\n6.  Recover Land   | 16. Gifting\n7.  Eating         | 17. Open Inventory\n8.  Sleeping       | 18. Show Time\n9.  Cooking        | 19. Show Location\n10. Fishing        | 20. Selling (to Shipping Bin)\n21. Exit to Main Menu");
                System.out.print(">> ");
                String input = scanner.nextLine().trim().toLowerCase();

                Action selectedAction = null; 
                NPC targetNpcForAction = null;
                // Variabel untuk statistik sebelum aksi, jika diperlukan (sudah ada di kode Anda)
                // int fishInInventoryBeforeAction = countItemsOfType(Fish.class);
                // int cropsInInventoryBeforeAction = countItemsOfType(Crops.class);

                switch (input) { //
                    case "1": selectedAction = actionMap.get("moving"); break;
                    case "2": selectedAction = actionMap.get("tilling"); break;
                    case "3": selectedAction = actionMap.get("planting"); break;
                    case "4": selectedAction = actionMap.get("watering"); break;
                    case "5": selectedAction = actionMap.get("harvesting"); break;
                    case "6": selectedAction = actionMap.get("recover land"); break;
                    case "7": selectedAction = actionMap.get("eating"); break;
                    case "8": selectedAction = actionMap.get("sleeping"); break;
                    case "9": performCookingUI(); break;
                    case "10": selectedAction = actionMap.get("fishing"); break;
                    case "11": 
                        NPC npcAtLocation = getNPCInSameLocation(player);
                        if (npcAtLocation == null) {
                            System.out.println("Kamu harus berada di lokasi yang sama dengan NPC untuk melamar (rumah NPC).");
                            try {
                                Thread.sleep(1500); // Delay untuk memberi waktu pesan tampil
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            clearScreen();
                        } else if(player.getLocation_inworld().equalsIgnoreCase("farm")) {
                            System.out.println("Mana bisa lamaran jarak jauh begitu, samperin ke rumahnya gih");
                            break;
                        } else {
                            selectedAction = new Proposing(npcAtLocation);
                            targetNpcForAction = npcAtLocation;
                        }
                        break;
                    case "12":
                        npcAtLocation = getNPCInSameLocation(player);
                        if (npcAtLocation == null) {
                            System.out.println("Kamu harus berada di lokasi yang sama dengan NPC untuk menikahi (rumah NPC).");
                            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                            clearScreen();
                        } else if(player.getLocation_inworld().equalsIgnoreCase("farm")) {
                            System.out.println("Mana bisa menikah jarak jauh begitu, samperin ke rumahnya gih");
                            break;
                        } else {
                            selectedAction = new Marry(npcAtLocation);
                            targetNpcForAction = npcAtLocation;
                        }
                        break;
                    case "13": selectedAction = actionMap.get("watching tv"); break;
                    case "14": performVisiting(); break;
                    case "15":
                        npcAtLocation = getNPCInSameLocation(player);
                        if (npcAtLocation == null) {
                            System.out.println("Kamu harus berada di lokasi yang sama dengan NPC untuk mengobrol (rumah NPC).");
                            try {
                                Thread.sleep(1500); 
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                            clearScreen();
                        } else {
                            selectedAction = new Chatting(npcAtLocation);
                            targetNpcForAction = npcAtLocation;
                        }
                        break;
                    case "16":
                        if(!player.getLocation_inworld().equalsIgnoreCase("Farm")) {
                            targetNpcForAction = selectNpcForInteraction("memberi hadiah");
                            if (targetNpcForAction != null) {
                                Items itemToGift = selectItemFromInventory("memberi hadiah kepada " + targetNpcForAction.getName());
                                if (itemToGift != null) selectedAction = new Gifting(targetNpcForAction, itemToGift);
                                else System.out.println("Pemberian hadiah dibatalkan.");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                                clearScreen();
                            } break;
                        }
                        else {
                            System.out.println("Kan di farm anda cuma sendiri, orang-orang kan pada hidup di tempat lain. Coba Main ke sana dulu gih");
                            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                            break; 
                        }
                    case "17": selectedAction = actionMap.get("open inventory"); break;
                    case "18": selectedAction = actionMap.get("show time"); break;
                    case "19": selectedAction = actionMap.get("show location"); break;
                    case "20": selectedAction = actionMap.get("selling"); break;
                    case "21": 
                        inGame = false; // Keluar dari loop game ini, akan kembali ke main menu
                        clearScreen();
                        break;
                    default: System.out.println("Pilihan aksi tidak valid."); clearScreen();
                }

                if (selectedAction != null) {
                    boolean canPerform = true;
                    if (!(selectedAction instanceof Sleeping) && selectedAction.getEnergyCost() > 0) {
                        if (player.getEnergy() < -20 + selectedAction.getEnergyCost()) {
                            System.out.println("Energi tidak cukup untuk melakukan aksi ini. Energi tersisa: " + player.getEnergy() + ", dibutuhkan: " + selectedAction.getEnergyCost());
                            canPerform = false;
                        }
                    }

                    if (canPerform) {
                        int currentDay = gameTime.getCurrDay();
                        int fishCaughtBeforeAction = totalFishCaught; // Untuk unlock resep Sashimi
                        String lastFishNameCaught = ""; // Untuk Fugu & Legend. Ini perlu di-set oleh Fishing.perform()
                        boolean hadHotPepperBefore = player.haveitem("Hot Pepper"); 
                        

                        // Simpan nama ikan yang ditangkap jika aksi adalah memancing
                        // Ini cara sederhana, idealnya Fishing.perform() mengembalikan hasil tangkapan
                        Items potentialCatch = null; 
                        synchronized (gameTime) { 
                            selectedAction.perform(player, gameTime);
                        }

                        if (selectedAction instanceof Fishing) {
                            Fishing fishingSelectedAction = (Fishing) selectedAction;
                            if(fishingSelectedAction.isFishFinallyCaught()) {
                                Fish caughtFish = fishingSelectedAction.getSelectedFish();
                                if(caughtFish != null)  {
                                    incrementFishCaught(caughtFish.getFishType(), caughtFish.getName());
                                }
                            }
                        }

                        if (selectedAction instanceof Harvesting) {
                            int qty = 1;    
                            qty = ((Harvesting) selectedAction).getAmountHarvested();    
                            incrementCropHarvested(qty);
                        }

                        if (!hadHotPepperBefore && player.haveitem("Hot Pepper")) {
                            unlockRecipe("Fish Stew");
                        }

                        if (selectedAction instanceof Sleeping) {
                            if(gameTime.getCurrDay() > currentDay) {
                                updateDailyStatsAndSell();
                            }
                        }
                        if (targetNpcForAction != null) { 
                            if (selectedAction instanceof Chatting && chatFrequency != null) {
                                chatFrequency.put(targetNpcForAction, chatFrequency.getOrDefault(targetNpcForAction, 0) + 1);
                            } else if (selectedAction instanceof Gifting && giftFrequency != null) {
                                 giftFrequency.put(targetNpcForAction, giftFrequency.getOrDefault(targetNpcForAction, 0) + 1);
                            }
                        }
                    }

                    else {
                        synchronized (gameTime) { // Sinkronisasi jika aksi memodifikasi gameTime
                             selectedAction.perform(player, gameTime); // Aksi akan memanggil gameTime.addTime() atau gameTime.timeSkip()
                        }
                        if (selectedAction instanceof Sleeping) {
                            updateDailyStatsAndSell();
                        }
                        // Update statistik lainnya jika perlu
                        if (selectedAction instanceof Chatting && targetNpcForAction != null) chatFrequency.put(targetNpcForAction, chatFrequency.getOrDefault(targetNpcForAction, 0) + 1);
                        if (selectedAction instanceof Gifting && targetNpcForAction != null) giftFrequency.put(targetNpcForAction, giftFrequency.getOrDefault(targetNpcForAction, 0) + 1);
                    }
                }

                if (inGame) {
                    checkEndGameConditions(false);
                }
            }
        } finally {
            stopPassiveTimeFlow(); // Pastikan timer dihentikan saat keluar dari inGameLoop
        }
    }

    private void performCookingUI() {
        if (player == null || recipes == null || gameTime == null || player.getInventory() == null) {
            System.out.println("Error: Data untuk memasak tidak lengkap.");
            return;
        }
        if (!player.getLocation_inworld().equalsIgnoreCase("Farm") ||
            (player.getFarmname() == null || player.getFarmname().getFarmMap() == null ||
             !player.getFarmname().getFarmMap().isPlayerInHouse(player.getLocation_infarm()))) {
            System.out.println("Kamu harus berada di dalam rumah untuk bisa memasak.");
            return;
        }

        System.out.println("Pilih resep untuk dimasak (hanya resep yang sudah terbuka):");
        List<Recipe> availableRecipes = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r != null && r.getStatus()) {
                availableRecipes.add(r);
            }
        }

        if (availableRecipes.isEmpty()) {
            System.out.println("Tidak ada resep yang sudah terbuka atau bisa kamu masak saat ini.");
            return;
        }

        for (int i = 0; i < availableRecipes.size(); i++) {
            System.out.println((i + 1) + ". " + availableRecipes.get(i).getName());
        }
        System.out.println((availableRecipes.size() + 1) + ". Batal");
        System.out.print(">> ");
        
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) {
            System.out.println("Input kosong, memasak dibatalkan.");
            player.setEnergy(player.getEnergy() - 10);
            return;
        }
        int recipeChoiceInput;
        try {
            recipeChoiceInput = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid. Harap masukkan angka.");
            return;
        }

        if (recipeChoiceInput == availableRecipes.size() + 1) {
            System.out.println("Memasak dibatalkan.");
            return;
        }

        if (recipeChoiceInput > 0 && recipeChoiceInput <= availableRecipes.size()) {
            Recipe selectedRecipe = availableRecipes.get(recipeChoiceInput - 1);
            
            System.out.println("Pilih bahan bakar (Ketik 'Firewood' atau 'Coal', atau 'batal'):");
            String fuelTypeInput = scanner.nextLine().trim();

            if (fuelTypeInput.equalsIgnoreCase("batal")) {
                System.out.println("Memasak dibatalkan.");
                return;
            }

            String fuelType = null;
            if (fuelTypeInput.equalsIgnoreCase("Firewood")) {
                fuelType = "Firewood";
            } else if (fuelTypeInput.equalsIgnoreCase("Coal")) {
                fuelType = "Coal";
            } else {
                System.out.println("Jenis bahan bakar tidak valid ('Firewood' atau 'Coal').");
                return;
            }
            
            // Buat instance Cooking Action dan panggil perform
            // Teruskan 'this' (instance Game) ke konstruktor Cooking
            Cooking cookingAction = new Cooking(selectedRecipe, fuelType); 
            cookingAction.perform(player, gameTime); 

        } else {
            System.out.println("Pilihan resep tidak valid.");
        }
    }
    
    // ... (method interactWithStore, displayAndBuySeeds, displayAndBuyRecipes tetap sama) ...
    private void interactWithStore() {
        boolean shopping = true;
        System.out.println("\n================================");
        System.out.println("Selamat datang di Toko Spakbor!");
        System.out.println("Gold Anda: " + player.getGold() + "g");
        System.out.println("================================");

        while (shopping) {
            System.out.println("\nApa yang ingin Anda lakukan di toko?");
            System.out.println("1. Beli Bibit");
            System.out.println("2. Beli Resep");
            System.out.println("3. Beli Fuel (Firewood/Coal)");
            System.out.println("4. Beli cincin");
            System.out.println("5. Interaksi dengan Emily");
            System.out.println("6. Keluar Toko");
            System.out.print(">> ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    displayAndBuySeeds();
                    break;
                case "2":
                    displayAndBuyRecipes();
                    break;
                case "3":
                    displayAndBuyFuel();
                    break;
                case "4":
                    System.out.println("\n--- Cincin Tersedia untuk Dibeli ---");
                    List<Items> ringsForSale = new ArrayList<>();
                    if (this.allGameItems != null) {
                        for (Items item : this.allGameItems) {
                            if (item instanceof Misc && item.getName().equalsIgnoreCase("Proposal Ring")) {
                                ringsForSale.add(item);
                            }
                        }
                    }
                    if (ringsForSale.isEmpty()) {
                        System.out.println("Maaf, tidak ada cincin yang tersedia untuk dijual saat ini.");
                        break;
                    }
                    for (int i = 0; i < ringsForSale.size(); i++) {
                        Items ring = ringsForSale.get(i);
                        System.out.println((i + 1) + ". " + ring.getName() + " - Harga: " + ((Misc)ring).getHargaBeli() + "g");
                    }
                    System.out.println((ringsForSale.size() + 1) + ". Kembali ke Menu Toko");
                    System.out.print("Pilih cincin untuk dibeli (nomor): ");
                    String ringInput = scanner.nextLine().trim();
                    try {
                        int ringSelection = Integer.parseInt(ringInput);
                        if (ringSelection > 0 && ringSelection <= ringsForSale.size()) {
                            Items selectedRing = ringsForSale.get(ringSelection - 1);
                            System.out.print("Berapa banyak " + selectedRing.getName() + " yang ingin Anda beli? (Angka) ");
                            String qtyInput = scanner.nextLine().trim();
                            try {
                                int quantity = Integer.parseInt(qtyInput);
                                if (quantity <= 0) {
                                    System.out.println("Jumlah pembelian harus lebih dari 0.");
                                    continue;
                                }
                                int totalCost = ((Misc)selectedRing).getHargaBeli() * quantity;
                                if (player.getGold() >= totalCost) {
                                    player.setGold(player.getGold() - totalCost);
                                    player.addInventory(selectedRing, quantity);
                                    System.out.println("Anda berhasil membeli " + quantity + " " + selectedRing.getName() + " seharga " + totalCost + "g.");
                                    this.totalExpenditure += totalCost;
                                } else {
                                    System.out.println("Maaf, Gold Anda tidak cukup. Butuh " + totalCost + "g.");
                                }
                            } catch (NumberFormatException eQty) {
                                System.out.println("Input jumlah tidak valid. Harap masukkan angka.");
                            }
                        } else if (ringSelection == ringsForSale.size() + 1) {
                            // Kembali ke menu toko
                        } else {
                            System.out.println("Pilihan cincin tidak valid.");
                        }
                    } catch (NumberFormatException eSelect) {
                        System.out.println("Input pilihan tidak valid. Harap masukkan nomor.");
                    }
                    break;

                case "5":
                    System.out.println("Anda memilih untuk berinteraksi dengan Emily.");
                    System.out.println("1. Chatting");
                    System.out.println("2. Gifting");
                    System.out.print("Pilih aksi (1/2): ");
                    String actionChoice = scanner.nextLine().trim();
                    Action selectedAction = null;
                    if (actionChoice.equals("1")) {
                        NPC emily = findNpcByName("Emily");
                        if (emily != null) {
                            selectedAction = new Chatting(emily);
                            selectedAction.perform(player, gameTime);
                            chatFrequency.put(emily, chatFrequency.getOrDefault(emily, 0) + 1);
                            continue;
                        }
                    } else if (actionChoice.equals("2")) {
                        Items giftItem = selectItemFromInventory("memberi hadiah kepada Emily");
                        if (giftItem != null) {
                            selectedAction = new Gifting(findNpcByName("Emily"), giftItem);
                            selectedAction.perform(player, gameTime);
                            giftFrequency.put(findNpcByName("Emily"), giftFrequency.getOrDefault(findNpcByName("Emily"), 0) + 1);
                            continue;
                        } else {
                            System.out.println("Pemberian hadiah dibatalkan.");
                            continue; // Kembali ke menu toko
                        }
                    } else {
                        System.out.println("Pilihan tidak valid. Kembali ke menu toko.");
                        continue; // Kembali ke menu toko
                    }
                case "6":
                    System.out.println("Terima kasih sudah berkunjung! Datang lagi ya!");
                    shopping = false;
                    performVisiting();
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
            if (shopping) {
                System.out.println("\nGold Anda saat ini: " + player.getGold() + "g");
            }
        }
    }
    private void displayAndBuyFuel() {
        System.out.println("\n--- Bahan Bakar Tersedia untuk Dibeli ---");
        List<Items> fuelItems = new ArrayList<>();
        if (this.allGameItems != null) {
            for (Items item : this.allGameItems) {
                if (item instanceof Misc && (item.getName().equalsIgnoreCase("Firewood") || item.getName().equalsIgnoreCase("Coal"))) {
                    fuelItems.add(item);
                }
            }
        }

        if (fuelItems.isEmpty()) {
            System.out.println("Maaf, tidak ada bahan bakar yang tersedia untuk dijual saat ini.");
            return;
        }

        for (int i = 0; i < fuelItems.size(); i++) {
            Items fuel = fuelItems.get(i);
            System.out.println((i + 1) + ". " + fuel.getName() + " - Harga: " + ((Misc)fuel).getHargaBeli() + "g");
        }
        System.out.println((fuelItems.size() + 1) + ". Kembali ke Menu Toko");
        System.out.print("Pilih bahan bakar untuk dibeli (nomor): ");

        String input = scanner.nextLine().trim();
        try {
            int selection = Integer.parseInt(input);
            if (selection > 0 && selection <= fuelItems.size()) {
                Items selectedFuel = fuelItems.get(selection - 1);
                System.out.print("Berapa banyak " + selectedFuel.getName() + " yang ingin Anda beli? (Angka) ");
                String qtyInput = scanner.nextLine().trim();
                try {
                    int quantity = Integer.parseInt(qtyInput);
                    if (quantity <= 0) {
                        System.out.println("Jumlah pembelian harus lebih dari 0.");
                        return;
                    }
                    int totalCost = ((Misc)selectedFuel).getHargaBeli() * quantity;
                    if (player.getGold() >= totalCost) {
                        player.setGold(player.getGold() - totalCost);
                        player.addInventory(selectedFuel, quantity);
                        System.out.println("Anda berhasil membeli " + quantity + " " + selectedFuel.getName() + " seharga " + totalCost + "g.");
                        this.totalExpenditure += totalCost;
                    } else {
                        System.out.println("Maaf, Gold Anda tidak cukup. Butuh " + totalCost + "g.");
                    }
                } catch (NumberFormatException eQty) {
                    System.out.println("Input jumlah tidak valid. Harap masukkan angka.");
                }
            } else if (selection == fuelItems.size() + 1) {
                // Kembali
            } else {
                System.out.println("Pilihan bahan bakar tidak valid.");
            }
        } catch (NumberFormatException eSelect) {
            System.out.println("Input pilihan tidak valid. Harap masukkan nomor.");
        }
    }

    private void displayAndBuySeeds() {
        System.out.println("\n--- Bibit Tersedia untuk Dibeli ---");
        List<Seeds> seedsForSale = new ArrayList<>();
        if (this.allGameItems != null) {
            for (Items item : this.allGameItems) {
                if (item instanceof Seeds) {
                    seedsForSale.add((Seeds) item);
                }
            }
        }

        if (seedsForSale.isEmpty()) {
            System.out.println("Maaf, tidak ada bibit yang tersedia untuk dijual saat ini.");
            return;
        }

        for (int i = 0; i < seedsForSale.size(); i++) {
            Seeds seed = seedsForSale.get(i);
            System.out.println((i + 1) + ". " + seed.getName() +
                               " - Harga: " + seed.getHargaBeli() + "g " +
                               "(Musim: " + seed.getSeason() + ", Panen: " + seed.getDaystoharvest() + " hari)");
        }
        System.out.println((seedsForSale.size() + 1) + ". Kembali ke Menu Toko");
        System.out.print("Pilih bibit untuk dibeli (nomor): ");

        String input = scanner.nextLine().trim();
        try {
            int selection = Integer.parseInt(input);
            if (selection > 0 && selection <= seedsForSale.size()) {
                Seeds selectedSeed = seedsForSale.get(selection - 1);
                System.out.print("Berapa banyak " + selectedSeed.getName() + " yang ingin Anda beli? (Angka) ");
                String qtyInput = scanner.nextLine().trim();
                try {
                    int quantity = Integer.parseInt(qtyInput);
                    if (quantity <= 0) {
                        System.out.println("Jumlah pembelian harus lebih dari 0.");
                        return;
                    }
                    int totalCost = selectedSeed.getHargaBeli() * quantity;
                    if (player.getGold() >= totalCost) {
                        player.setGold(player.getGold() - totalCost);
                        player.addInventory(selectedSeed, quantity);
                        System.out.println("Anda berhasil membeli " + quantity + " " + selectedSeed.getName() + " seharga " + totalCost + "g.");
                        this.totalExpenditure += totalCost;
                    } else {
                        System.out.println("Maaf, Gold Anda tidak cukup. Butuh " + totalCost + "g.");
                    }
                } catch (NumberFormatException eQty) {
                    System.out.println("Input jumlah tidak valid. Harap masukkan angka.");
                }
            } else if (selection == seedsForSale.size() + 1) {
                // Kembali
            } else {
                System.out.println("Pilihan bibit tidak valid.");
            }
        } catch (NumberFormatException eSelect) {
            System.out.println("Input pilihan tidak valid. Harap masukkan nomor.");
        }
    }

    private void displayAndBuyRecipes() {
        System.out.println("\n--- Resep Tersedia untuk Dibeli ---");
        List<Recipe> recipesToDisplay = new ArrayList<>();
        if (this.storeRecipeNames != null && this.recipes != null) {
            for (String recipeName : this.storeRecipeNames) { 
                Recipe recipe = findRecipeByName(recipeName);
                if (recipe != null && !recipe.getStatus()) { 
                    recipesToDisplay.add(recipe);
                }
            }
        }

        if (recipesToDisplay.isEmpty()) {
            System.out.println("Tidak ada resep baru yang bisa Anda beli (mungkin sudah punya semua yang dijual).");
            return;
        }

        for (int i = 0; i < recipesToDisplay.size(); i++) {
            Recipe recipe = recipesToDisplay.get(i);
            System.out.println((i + 1) + ". " + recipe.getName() + " - Harga: " + DEFAULT_RECIPE_PRICE_IN_STORE + "g");
        }
        System.out.println((recipesToDisplay.size() + 1) + ". Kembali ke Menu Toko");
        System.out.print("Pilih resep untuk dibeli (nomor): ");

        String input = scanner.nextLine().trim();
        try {
            int selection = Integer.parseInt(input);
            if (selection > 0 && selection <= recipesToDisplay.size()) {
                Recipe selectedRecipe = recipesToDisplay.get(selection - 1);
                int recipeCost = DEFAULT_RECIPE_PRICE_IN_STORE;
                if (player.getGold() >= recipeCost) {
                    player.setGold(player.getGold() - recipeCost);
                    unlockRecipe(selectedRecipe.getName());
                    System.out.println("Anda berhasil membeli dan mempelajari resep: " + selectedRecipe.getName() + "!");
                    this.totalExpenditure += recipeCost;
                } else {
                    System.out.println("Maaf, Gold Anda tidak cukup.");
                }
            } else if (selection == recipesToDisplay.size() + 1) {
                // Kembali
            } else {
                System.out.println("Pilihan resep tidak valid.");
            }
        } catch (NumberFormatException eSelect) {
            System.out.println("Input pilihan tidak valid. Harap masukkan nomor.");
        }
    }

    private boolean firstHarvestDone = false; 
    public void incrementCropHarvested(int amount) { 
        this.totalCropsHarvested += amount;
        if (!firstHarvestDone && this.totalCropsHarvested > 0) {
            unlockRecipe("Veggie Soup");
            firstHarvestDone = true;
            System.out.println("Kamu merasa terinspirasi setelah panen pertamamu!");
        }
    }

    // Modifikasi untuk menerima nama ikan juga, agar bisa unlock resep Fugu dan Legend
    public void incrementFishCaught(String fishTypeCaught, String fishNameCaught) { 
        this.totalFishCaught++; 
        if(fishTypeCaught!=null) { // Hanya proses jika fishTypeCaught tidak null
            switch(fishTypeCaught.toLowerCase()){
                case "common":this.commonFishCaught++;break; 
                case "regular":this.regularFishCaught++;break; 
                case "legendary":this.legendaryFishCaught++;break;
            }
        }

        if (this.totalFishCaught >= 10) {
            unlockRecipe("Sashimi");
        }
        if (fishNameCaught != null) {
            if (fishNameCaught.equalsIgnoreCase("Pufferfish")) {
                unlockRecipe("Fugu");
                 System.out.println("Menangkap Pufferfish memberimu ide resep baru!");
            }
            if (fishNameCaught.equalsIgnoreCase("Legend")) {
                unlockRecipe("The Legends of Spakbor");
                 System.out.println("Menangkap ikan legendaris memberimu inspirasi resep agung!");
            }
        }
    }
    // Anda perlu memastikan bahwa Fishing.perform() memanggil:
    // gameInstance.incrementFishCaught(selectedFish.getFishType(), selectedFish.getName());
    // jika gameInstance diteruskan ke Fishing atau Fishing mengembalikan nama ikan yang ditangkap.

    private int countItemsOfType(Class<?> itemType) { //
        if (player == null || player.getInventory() == null) return 0;
        int count = 0;
        for (Map.Entry<Items, Integer> entry : player.getInventory().getItemsMap().entrySet()) {
            if (itemType.isInstance(entry.getKey())) count += entry.getValue();
        }
        return count;
    }

    private void updateDailyStatsAndSell() { //
        totalDaysPlayed++;
        performDailyPlantChecks(); 
        if (farm != null && farm.getFarmMap() != null && farm.getFarmMap().getShippingBin() != null && player != null) {
            float goldBeforeSell = player.getGold();
            farm.getFarmMap().getShippingBin().sellBin(player);
            totalIncome += (player.getGold() - goldBeforeSell);
            farm.getFarmMap().getShippingBin().resetBinDay();
        }
    }

    private Items selectItemFromInventory(String purpose) { //
        System.out.println("Pilih item dari inventory untuk " + purpose + ": (Ketik 'batal' untuk membatalkan)");
        if (player.getInventory().getItemsMap().isEmpty()) {
            System.out.println("Inventory kosong."); return null;
        }
        player.getInventory().printInventory();
        System.out.print("Masukkan nama item: "); String itemName = scanner.nextLine().trim();
        if (itemName.equalsIgnoreCase("batal")) return null;
        for (Items item : player.getInventory().getItemsMap().keySet()) {
            if (item.getName().equalsIgnoreCase(itemName) && player.getInventory().getItemsMap().get(item) > 0) return item;
        }
        System.out.println("Item tidak ditemukan atau jumlah tidak cukup."); return null;
    }

    private void performCooking() { //
        System.out.println("Pilih resep untuk dimasak:");
        if (recipes.isEmpty()) { System.out.println("Tidak ada resep yang diketahui."); return; }
        List<Recipe> availableRecipes = new ArrayList<>();
        for (Recipe r : recipes) if (r.getStatus()) availableRecipes.add(r);
        if(availableRecipes.isEmpty()){ System.out.println("Tidak ada resep yang sudah terbuka."); return; }
        for (int i = 0; i < availableRecipes.size(); i++) System.out.println((i + 1) + ". " + availableRecipes.get(i).getName());
        System.out.print(">> "); int recipeChoiceInput = -1;
        try { recipeChoiceInput = Integer.parseInt(scanner.nextLine()); }
        catch (NumberFormatException e) { System.out.println("Input tidak valid."); return; }

        if (recipeChoiceInput > 0 && recipeChoiceInput <= availableRecipes.size()) {
            Recipe selectedRecipe = availableRecipes.get(recipeChoiceInput - 1);
            System.out.println("Pilih bahan bakar (Firewood/Coal):"); String fuelType = scanner.nextLine().trim();
            Items fuelItem = null;
            if (fuelType.equalsIgnoreCase("Firewood")) fuelItem = findItemByName("Firewood");
            else if (fuelType.equalsIgnoreCase("Coal")) fuelItem = findItemByName("Coal");
            if (fuelItem != null) {
                 if (player.getInventory().getItemsMap().getOrDefault(fuelItem, 0) > 0) {
                    synchronized(gameTime) { // Sinkronisasi jika aksi memodifikasi gameTime
                        new Cooking(selectedRecipe, fuelType).perform(player, gameTime);
                    }
                 }
                 else System.out.println("Kamu tidak memiliki " + fuelType + " di inventory.");
            } else System.out.println("Jenis bahan bakar tidak valid atau item bahan bakar tidak ditemukan.");
        } else System.out.println("Pilihan resep tidak valid.");
    }

    private NPC selectNpcForInteraction(String interactionType) { //
        System.out.println("Pilih NPC untuk " + interactionType + ": (Ketik 'batal' untuk membatalkan)");
        if (npcs.isEmpty()) { System.out.println("Tidak ada NPC di game."); return null; }
        for (int i = 0; i < npcs.size(); i++) System.out.println((i + 1) + ". " + npcs.get(i).getName());
        System.out.print(">> "); String npcChoiceStr = scanner.nextLine().trim();
        if (npcChoiceStr.equalsIgnoreCase("batal")) return null; int npcChoiceInput = -1;
        try { npcChoiceInput = Integer.parseInt(npcChoiceStr); }
        catch (NumberFormatException e) { System.out.println("Input tidak valid."); return null; }
        if (npcChoiceInput > 0 && npcChoiceInput <= npcs.size()) return npcs.get(npcChoiceInput - 1);
        else { System.out.println("Pilihan NPC tidak valid."); return null; }
    }

    private void performVisiting() { //
        if(!player.getFarmname().getFarmMap().isEdgeTile(player.getLocation_infarm())) {
            System.out.println("Silakan pindah ke lokasi tepi farm.");
            return;
        }
        System.out.println("Pilih destinasi untuk dikunjungi (atau ketik 'Farm' untuk kembali ke farm):");
        worldMap.showPlaces(); 
        System.out.print(">> ");
        String destination = scanner.nextLine().trim();
        if (destination.equalsIgnoreCase(player.getLocation_inworld())) {
            System.out.println("Kamu sudah berada di lokasi tersebut.");
            return;
        }

        if (destination.equalsIgnoreCase("Store")) {
            new Visiting(destination, temp).perform(player, gameTime); 
            if (player.getLocation_inworld().equalsIgnoreCase("Store")) {
                interactWithStore();
            }
            return; 
        }

        if (!worldMap.isValidLocation(destination)) {
            System.out.println("'" + destination + "' bukan merupakan lokasi yang valid di World Map.");
            System.out.println("Silakan pilih dari daftar tempat yang tersedia atau ketik 'Farm'.");
            try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return;
        }

        if(destination.equalsIgnoreCase("Dasco's Gambling Den")) {
            new Visiting(destination, temp).perform(player, gameTime);
            System.out.println("Selamat datang di Dasco's Gambling Den! Silakan nikmati permainan yang ada di sini.");
            new MainJuday().start(player);
            return;
        }

        NPC targetNPC = findNpcByLocation(destination);
        temp = player.getLocation_infarm(); 
        new Visiting(destination,temp).perform(player, gameTime);

        if (targetNPC != null) {
            visitFrequency.put(targetNPC, visitFrequency.getOrDefault(targetNPC, 0) + 1);
            System.out.println("Visit frequency untuk " + targetNPC.getName() + " bertambah menjadi: " + visitFrequency.get(targetNPC));
        }
        return;
    }

    private NPC findNpcByLocation(String location) { //
        for (NPC npc : npcs) if (npc.getLocation().equalsIgnoreCase(location)) return npc;
        return null;
    }

    private void initializeItemsAndRecipes() { //
        allGameItems = new ArrayList<>();
        Crops parsnipCrop = new Crops("Parsnip", 35, 50, 1); Crops cauliflowerCrop = new Crops("Cauliflower", 150, 200, 1); Crops potatoCrop = new Crops("Potato", 80, 0, 1); Crops wheatCrop = new Crops("Wheat", 30, 50, 3); Crops blueberryCrop = new Crops("Blueberry", 40, 150, 3); Crops tomatoCrop = new Crops("Tomato", 60, 90, 1); Crops hotPepperCrop = new Crops("Hot Pepper", 40, 0, 1); Crops melonCrop = new Crops("Melon", 250, 0, 1); Crops cranberryCrop = new Crops("Cranberry", 25, 0, 10); Crops pumpkinCrop = new Crops("Pumpkin", 250, 300, 1); Crops grapeCrop = new Crops("Grape", 10, 100, 20); Crops eggplantCrop = new Crops("Eggplant", 80, 0, 1);
        allGameItems.addAll(Arrays.asList(parsnipCrop, cauliflowerCrop, potatoCrop, wheatCrop, blueberryCrop, tomatoCrop, hotPepperCrop, melonCrop, cranberryCrop, pumpkinCrop, grapeCrop, eggplantCrop));
        Seeds parsnipSeeds = new Seeds("Parsnip Seeds", Season.SPRING, 1, parsnipCrop, 10, 20, gameTime); Seeds cauliflowerSeeds = new Seeds("Cauliflower Seeds", Season.SPRING, 5, cauliflowerCrop, 40, 80, gameTime); Seeds potatoSeeds = new Seeds("Potato Seeds", Season.SPRING, 3, potatoCrop, 25, 50, gameTime); Seeds wheatSeedsSpring = new Seeds("Wheat Seeds (Spring)", Season.SPRING, 1, wheatCrop, 30, 60, gameTime); Seeds blueberrySeeds = new Seeds("Blueberry Seeds", Season.SUMMER, 7, blueberryCrop, 40, 80, gameTime); Seeds tomatoSeeds = new Seeds("Tomato Seeds", Season.SUMMER, 3, tomatoCrop, 25, 50, gameTime); Seeds hotPepperSeeds = new Seeds("Hot Pepper Seeds", Season.SUMMER, 1, hotPepperCrop, 20, 40, gameTime); Seeds melonSeeds = new Seeds("Melon Seeds", Season.SUMMER, 4, melonCrop, 40, 80, gameTime); Seeds cranberrySeeds = new Seeds("Cranberry Seeds", Season.FALL, 2, cranberryCrop, 50, 100, gameTime); Seeds pumpkinSeeds = new Seeds("Pumpkin Seeds", Season.FALL, 7, pumpkinCrop, 75, 150, gameTime); Seeds wheatSeedsFall = new Seeds("Wheat Seeds (Fall)", Season.FALL, 1, wheatCrop, 30, 60, gameTime); Seeds grapeSeeds = new Seeds("Grape Seeds", Season.FALL, 3, grapeCrop, 30, 60, gameTime);
        allGameItems.addAll(Arrays.asList(parsnipSeeds, cauliflowerSeeds, potatoSeeds, wheatSeedsSpring, blueberrySeeds, tomatoSeeds, hotPepperSeeds, melonSeeds, cranberrySeeds, pumpkinSeeds, wheatSeedsFall, grapeSeeds));
        Fish bullhead = new Fish("Bullhead", new Season[]{Season.ANY}, new Weather[]{Weather.ANY}, new String[]{"Mountain Lake"}, "Common", 40, 0, 24); Fish carp = new Fish("Carp", new Season[]{Season.ANY}, new Weather[]{Weather.ANY}, new String[]{"Mountain Lake", "Pond"}, "Common", 20, 0, 24); Fish chub = new Fish("Chub", new Season[]{Season.ANY}, new Weather[]{Weather.ANY}, new String[]{"Forest River", "Mountain Lake"}, "Common", 20, 0, 24); Fish largemouthBass = new Fish("Largemouth Bass", new Season[]{Season.ANY}, new Weather[]{Weather.ANY}, new String[]{"Mountain Lake"}, "Regular", 40, 6, 18); Fish rainbowTrout = new Fish("Rainbow Trout", new Season[]{Season.SUMMER}, new Weather[]{Weather.SUNNY}, new String[]{"Forest River", "Mountain Lake"}, "Regular", 160, 6, 18); Fish sturgeon = new Fish("Sturgeon", new Season[]{Season.SUMMER, Season.WINTER}, new Weather[]{Weather.ANY}, new String[]{"Mountain Lake"}, "Regular", 80, 6, 18); Fish midnightCarp = new Fish("Midnight Carp", new Season[]{Season.WINTER, Season.FALL}, new Weather[]{Weather.ANY}, new String[]{"Mountain Lake", "Pond"}, "Regular", 80, 20, 2); Fish flounder = new Fish("Flounder", new Season[]{Season.SPRING, Season.SUMMER}, new Weather[]{Weather.ANY}, new String[]{"Ocean"}, "Regular", 60, 6, 22); Fish halibut = new Fish("Halibut", new Season[]{Season.ANY}, new Weather[]{Weather.ANY}, new String[]{"Ocean"}, "Regular", 40, 6, 11); Fish octopus = new Fish("Octopus", new Season[]{Season.SUMMER}, new Weather[]{Weather.ANY}, new String[]{"Ocean"}, "Regular", 120, 6, 22); Fish pufferfish = new Fish("Pufferfish", new Season[]{Season.SUMMER}, new Weather[]{Weather.SUNNY}, new String[]{"Ocean"}, "Regular", 240, 0, 16); Fish sardine = new Fish("Sardine", new Season[]{Season.ANY}, new Weather[]{Weather.ANY}, new String[]{"Ocean"}, "Regular", 40, 6, 18); Fish superCucumber = new Fish("Super Cucumber", new Season[]{Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.ANY}, new String[]{"Ocean"}, "Regular", 80, 18, 2); Fish catfish = new Fish("Catfish", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL}, new Weather[]{Weather.RAINY}, new String[]{"Forest River", "Pond"}, "Regular", 40, 6, 22); Fish salmon = new Fish("Salmon", new Season[]{Season.FALL}, new Weather[]{Weather.ANY}, new String[]{"Forest River"}, "Regular", 160, 6, 18); Fish angler = new Fish("Angler", new Season[]{Season.FALL}, new Weather[]{Weather.ANY}, new String[]{"Pond"}, "Legendary", 800, 8, 20); Fish crimsonfish = new Fish("Crimsonfish", new Season[]{Season.SUMMER}, new Weather[]{Weather.ANY}, new String[]{"Ocean"}, "Legendary", 800, 8, 20); Fish glacierfish = new Fish("Glacierfish", new Season[]{Season.WINTER}, new Weather[]{Weather.ANY}, new String[]{"Forest River"}, "Legendary", 800, 8, 20); Fish legend = new Fish("Legend", new Season[]{Season.SPRING}, new Weather[]{Weather.RAINY}, new String[]{"Mountain Lake"}, "Legendary", 1600, 8, 20);
        allGameItems.addAll(Arrays.asList(bullhead, carp, chub, largemouthBass, rainbowTrout, sturgeon, midnightCarp, flounder, halibut, octopus, pufferfish, sardine, superCucumber, catfish, salmon, angler, crimsonfish, glacierfish, legend));
        Food fishNChipsFood = new Food("Fish n' Chips", 50, 135, 150); Food baguetteFood = new Food("Baguette", 25, 80, 100); Food sashimiFood = new Food("Sashimi", 70, 275, 300); Food fuguFood = new Food("Fugu", 50, 135, 0); Food wineFood = new Food("Wine", 20, 90, 100); Food pumpkinPieFood = new Food("Pumpkin Pie", 35, 100, 120); Food veggieSoupFood = new Food("Veggie Soup", 40, 120, 140); Food fishStewFood = new Food("Fish Stew", 70, 260, 280); Food spakborSaladFood = new Food("Spakbor Salad", 70, 250, 0); Food fishSandwichFood = new Food("Fish Sandwich", 50, 180, 200); Food theLegendsOfSpakborFood = new Food("The Legends of Spakbor", 100, 2000, 0); Food cookedPigsHeadFood = new Food("Cooked Pig's Head", 100, 0, 1000);
        allGameItems.addAll(Arrays.asList(fishNChipsFood, baguetteFood, sashimiFood, fuguFood, wineFood, pumpkinPieFood, veggieSoupFood, fishStewFood, spakborSaladFood, fishSandwichFood, theLegendsOfSpakborFood, cookedPigsHeadFood));
        Equipment hoe = new Equipment("Hoe"); Equipment wateringCan = new Equipment("Watering Can"); Equipment pickaxe = new Equipment("Pickaxe"); Equipment fishingRod = new Equipment("Fishing Rod"); Misc proposalRing = new Misc("Proposal Ring", 50, 100);
        allGameItems.addAll(Arrays.asList(hoe, wateringCan, pickaxe, fishingRod, proposalRing));
        Misc firewood = new Misc("Firewood", 5, 10); Misc coal = new Misc("Coal", 10, 20); Misc egg = new Misc("Egg", 15, 30);
        allGameItems.addAll(Arrays.asList(firewood, coal, egg));
        Items anyFishPlaceholder = findItemByName("AnyFishPlaceholder");
        Map<Items, Integer> fncIng = new HashMap<>(); if(anyFishPlaceholder!=null&&wheatCrop!=null&&potatoCrop!=null){fncIng.put(anyFishPlaceholder,2);fncIng.put(wheatCrop,1);fncIng.put(potatoCrop,1); Recipe r = new Recipe("Fish n' Chips",fncIng,fishNChipsFood); r.setStatus(false);recipes.add(r);}
        Map<Items, Integer> bagIng = new HashMap<>(); if(wheatCrop!=null){bagIng.put(wheatCrop,3); Recipe r = new Recipe("Baguette",bagIng,baguetteFood);r.setStatus(true);recipes.add(r);}
        Map<Items, Integer> sashIng = new HashMap<>(); if(salmon!=null){sashIng.put(salmon,3); Recipe r = new Recipe("Sashimi",sashIng,sashimiFood);r.setStatus(false);recipes.add(r);}
        Map<Items, Integer> fuguIng = new HashMap<>(); if(pufferfish!=null){fuguIng.put(pufferfish,1); Recipe r = new Recipe("Fugu",fuguIng,fuguFood);r.setStatus(false);recipes.add(r);}
        Map<Items, Integer> wineIng = new HashMap<>(); if(grapeCrop!=null){wineIng.put(grapeCrop,2); Recipe r = new Recipe("Wine",wineIng,wineFood);r.setStatus(true);recipes.add(r);}
        Map<Items, Integer> ppIng = new HashMap<>(); if(egg!=null&&wheatCrop!=null&&pumpkinCrop!=null){ppIng.put(egg,1);ppIng.put(wheatCrop,1);ppIng.put(pumpkinCrop,1); Recipe r = new Recipe("Pumpkin Pie",ppIng,pumpkinPieFood);r.setStatus(true);recipes.add(r);}
        Map<Items, Integer> vsIng = new HashMap<>(); if(cauliflowerCrop!=null&&parsnipCrop!=null&&potatoCrop!=null&&tomatoCrop!=null){vsIng.put(cauliflowerCrop,1);vsIng.put(parsnipCrop,1);vsIng.put(potatoCrop,1);vsIng.put(tomatoCrop,1); Recipe r = new Recipe("Veggie Soup",vsIng,veggieSoupFood);r.setStatus(false);recipes.add(r);}
        Map<Items, Integer> fsIng = new HashMap<>(); if(anyFishPlaceholder!=null&&hotPepperCrop!=null&&cauliflowerCrop!=null){fsIng.put(anyFishPlaceholder,2);fsIng.put(hotPepperCrop,1);fsIng.put(cauliflowerCrop,2); Recipe r = new Recipe("Fish Stew",fsIng,fishStewFood);r.setStatus(false);recipes.add(r);}
        Map<Items, Integer> ssIng = new HashMap<>(); if(melonCrop!=null&&cranberryCrop!=null&&blueberryCrop!=null&&tomatoCrop!=null){ssIng.put(melonCrop,1);ssIng.put(cranberryCrop,1);ssIng.put(blueberryCrop,1);ssIng.put(tomatoCrop,1); Recipe r = new Recipe("Spakbor Salad",ssIng,spakborSaladFood);r.setStatus(true);recipes.add(r);}
        Map<Items, Integer> fishSwIng = new HashMap<>(); if(anyFishPlaceholder!=null&&wheatCrop!=null&&tomatoCrop!=null&&hotPepperCrop!=null){fishSwIng.put(anyFishPlaceholder,1);fishSwIng.put(wheatCrop,2);fishSwIng.put(tomatoCrop,1);fishSwIng.put(hotPepperCrop,1); Recipe r = new Recipe("Fish Sandwich",fishSwIng,fishSandwichFood);r.setStatus(false);recipes.add(r);}
        Map<Items, Integer> losIng = new HashMap<>(); if(legend!=null&&potatoCrop!=null&&parsnipCrop!=null&&tomatoCrop!=null&&eggplantCrop!=null){losIng.put(legend,1);losIng.put(potatoCrop,2);losIng.put(parsnipCrop,1);losIng.put(tomatoCrop,1);losIng.put(eggplantCrop,1); Recipe r = new Recipe("The Legends of Spakbor",losIng,theLegendsOfSpakborFood);r.setStatus(false);recipes.add(r);}
    }

    private void initializeNPCs() { //
        Items legendFish=findItemByName("Legend"),anglerFish=findItemByName("Angler"),crimsonfishFish=findItemByName("Crimsonfish"),glacierfishFish=findItemByName("Glacierfish"),firewoodMisc=findItemByName("Firewood"),coalMisc=findItemByName("Coal"),potatoCropItem=findItemByName("Potato"),wheatCropItem=findItemByName("Wheat"),hotPepperCropItem=findItemByName("Hot Pepper"),cranberryCropItem=findItemByName("Cranberry"),blueberryCropItem=findItemByName("Blueberry"),wineFoodItem=findItemByName("Wine"),theLegendsOfSpakborFoodItem=findItemByName("The Legends of Spakbor"),cookedPigsHeadFoodItem=findItemByName("Cooked Pig's Head"),fuguFoodItem=findItemByName("Fugu"),spakborSaladFoodItem=findItemByName("Spakbor Salad"),fishSandwichFoodItem=findItemByName("Fish Sandwich"),fishStewFoodItem=findItemByName("Fish Stew"),baguetteFoodItem=findItemByName("Baguette"),fishNChipsFoodItem=findItemByName("Fish n' Chips"),grapeCropItem=findItemByName("Grape"),cauliflowerCropItem=findItemByName("Cauliflower"),pufferfishFish=findItemByName("Pufferfish"),salmonFish=findItemByName("Salmon"),catfishFish=findItemByName("Catfish"),sardineFish=findItemByName("Sardine"),melonCropItem=findItemByName("Melon"),pumpkinCropItem=findItemByName("Pumpkin"),parsnipCropItem=findItemByName("Parsnip"),pumpkinPieFoodItem=findItemByName("Pumpkin Pie");
        List<Items> mayorLoved=(legendFish!=null)?new ArrayList<>(Collections.singletonList(legendFish)):new ArrayList<>(); List<Items> mayorLiked=new ArrayList<>(Arrays.asList(anglerFish,crimsonfishFish,glacierfishFish)); mayorLiked.removeIf(item->item==null); npcs.add(new NPC("Mayor Tadi",0,mayorLoved,mayorLiked,new ArrayList<>(),"single","Mayor's House",0));
        List<Items> carolineLoved=new ArrayList<>(Arrays.asList(firewoodMisc,coalMisc)); carolineLoved.removeIf(item->item==null); List<Items> carolineLiked=new ArrayList<>(Arrays.asList(potatoCropItem,wheatCropItem)); carolineLiked.removeIf(item->item==null); List<Items> carolineHated=(hotPepperCropItem!=null)?new ArrayList<>(Collections.singletonList(hotPepperCropItem)):new ArrayList<>(); npcs.add(new NPC("Caroline",0,carolineLoved,carolineLiked,carolineHated,"single","Caroline's House",0));
        List<Items> perryLoved=new ArrayList<>(Arrays.asList(cranberryCropItem,blueberryCropItem)); perryLoved.removeIf(item->item==null); List<Items> perryLiked=(wineFoodItem!=null)?new ArrayList<>(Collections.singletonList(wineFoodItem)):new ArrayList<>(); List<Items> perryHated=new ArrayList<>(); allGameItems.stream().filter(item->item instanceof Fish).forEach(perryHated::add); npcs.add(new NPC("Perry",0,perryLoved,perryLiked,perryHated,"single","Perry's House",0));
        List<Items> dascoLoved=new ArrayList<>(Arrays.asList(theLegendsOfSpakborFoodItem,cookedPigsHeadFoodItem,wineFoodItem,fuguFoodItem,spakborSaladFoodItem)); dascoLoved.removeIf(item->item==null); List<Items> dascoLiked=new ArrayList<>(Arrays.asList(fishSandwichFoodItem,fishStewFoodItem,baguetteFoodItem,fishNChipsFoodItem)); dascoLiked.removeIf(item->item==null); List<Items> dascoHated=new ArrayList<>(Arrays.asList(legendFish,grapeCropItem,cauliflowerCropItem,wheatCropItem,pufferfishFish,salmonFish)); dascoHated.removeIf(item->item==null); npcs.add(new NPC("Dasco",0,dascoLoved,dascoLiked,dascoHated,"single","Dasco's House",0));
        List<Items> emilyLoved=new ArrayList<>(); allGameItems.stream().filter(item->item instanceof Seeds).forEach(emilyLoved::add); List<Items> emilyLiked=new ArrayList<>(Arrays.asList(catfishFish,salmonFish,sardineFish)); emilyLiked.removeIf(item->item==null); List<Items> emilyHated=new ArrayList<>(Arrays.asList(coalMisc,firewoodMisc)); emilyHated.removeIf(item->item==null); npcs.add(new NPC("Emily",0,emilyLoved,emilyLiked,emilyHated,"single","Store",0));
        List<Items> abigailLoved=new ArrayList<>(Arrays.asList(blueberryCropItem,melonCropItem,pumpkinCropItem,grapeCropItem,cranberryCropItem)); abigailLoved.removeIf(item->item==null); List<Items> abigailLiked=new ArrayList<>(Arrays.asList(baguetteFoodItem,pumpkinPieFoodItem,wineFoodItem)); abigailLiked.removeIf(item->item==null); List<Items> abigailHated=new ArrayList<>(Arrays.asList(hotPepperCropItem,cauliflowerCropItem,parsnipCropItem,wheatCropItem)); abigailHated.removeIf(item->item==null); npcs.add(new NPC("Abigail",0,abigailLoved,abigailLiked,abigailHated,"single","Abigail's House",0));
        for(NPC npc:npcs){chatFrequency.put(npc,0);giftFrequency.put(npc,0);visitFrequency.put(npc,0);}
    }

    private void initializeActions() { //
        actionMap.put("moving", new Moving()); actionMap.put("tilling", new Tilling()); actionMap.put("planting", new Planting()); actionMap.put("watering", new Watering());
        actionMap.put("harvesting", new Harvesting()); actionMap.put("recover land", new Recover()); actionMap.put("eating", new Eating());
        actionMap.put("sleeping", new Sleeping()); actionMap.put("fishing", new Fishing()); actionMap.put("watching tv", new Watching());
        actionMap.put("open inventory", new OpenInventory()); actionMap.put("show time", new ShowTime()); actionMap.put("show location", new ShowLocation());
        actionMap.put("selling", new Selling());
    }

    public void incrementFishCaught(String fishType) { //
        this.totalFishCaught++; if(fishType==null)return;
        switch(fishType.toLowerCase()){case "common":this.commonFishCaught++;break; case "regular":this.regularFishCaught++;break; case "legendary":this.legendaryFishCaught++;break;}
    }

    public void showHelp() { //
        System.out.println("\n=== BANTUAN ===\nSelamat datang di Spakbor Hills! Game simulasi pertanian oleh Dr. Asep Spakbor.\nTujuan utama: Capai 17.209g atau Menikah dengan salah satu NPC.\nGunakan berbagai aksi untuk bertani, memancing, memasak, dan berinteraksi.\nSetiap aksi membutuhkan energi dan waktu. Atur energimu dengan baik!\nMusim berganti setiap 10 hari, perhatikan tanaman dan ikan yang tersedia.\nCuaca bisa hujan atau cerah, hujan akan menyiram tanamanmu secara otomatis.\nUntuk informasi lebih lanjut, lihat spesifikasi atau coba berbagai aksi!");
    }

    public void viewPlayerInfo() { //
        if (player != null) player.showStatus();
        else System.out.println("Belum ada pemain yang aktif. Mulai 'New Game' terlebih dahulu.");
    }
    private NPC getNPCInSameLocation(Player player) {
        for (NPC npc : npcs) {
            if (npc.getLocation().equalsIgnoreCase(player.getLocation_inworld())) {
                return npc;
            }
        }
        return null;
    }
    
    public void showStatistics() { //
        if (player == null) { System.out.println("Mulai permainan baru terlebih dahulu untuk melihat statistik."); return; }
        System.out.println("\n=== STATISTIK PEMAIN (" + player.getName() + ") ===");
        System.out.println("Total Income: " + String.format("%.0f", (double)totalIncome) + "g");
        System.out.println("Total Expenditure: " + String.format("%.0f", (double)totalExpenditure) + "g");
        if (totalDaysPlayed > 0) {
            double avgSeasonIncome = (totalIncome / Math.max(1.0, totalDaysPlayed / 10.0));
            double avgSeasonExpenditure = (totalExpenditure / Math.max(1.0, totalDaysPlayed / 10.0));
            System.out.println("Average Season Income: " + String.format("%.2f", avgSeasonIncome) + "g");
            System.out.println("Average Season Expenditure: " + String.format("%.2f", avgSeasonExpenditure) + "g");
        } else { System.out.println("Average Season Income: N/A\nAverage Season Expenditure: N/A"); }
        System.out.println("Total Days Played: " + totalDaysPlayed);
        System.out.println("\nNPCs Status:");
        if (npcs.isEmpty()) System.out.println("  Tidak ada data NPC.");
        else for (NPC npc : npcs) System.out.printf("  - %s:\n    Relationship Status: %s\n    Heart Points: %d\n    Chatting Frequency: %d\n    Gifting Frequency: %d\n    Visiting Frequency: %d\n", npc.getName(), npc.getRelationshipStatus(), npc.getHeartpoints(), chatFrequency.getOrDefault(npc,0), giftFrequency.getOrDefault(npc,0), visitFrequency.getOrDefault(npc,0));
        System.out.printf("\nCrops Harvested: %d\nFish Caught: %d\n  (Common: %d, Regular: %d, Legendary: %d)\n", totalCropsHarvested, totalFishCaught, commonFishCaught, regularFishCaught, legendaryFishCaught);
    }

    private void checkEndGameConditions(boolean forceShowStats) { //
        if (player == null) return;
        boolean currentGoldMilestone = player.getGold() >= 17209;
        boolean currentMarriageMilestone = player.getPartner() != null && player.getPartner().getRelationshipStatus().equalsIgnoreCase("spouse");
        boolean newMilestoneHit = false;
        if (currentGoldMilestone && !goldMilestoneReached) { goldMilestoneReached = true; newMilestoneHit = true; }
        if (currentMarriageMilestone && !marriageMilestoneReached) { marriageMilestoneReached = true; newMilestoneHit = true; }
        if (newMilestoneHit || (forceShowStats && (goldMilestoneReached || marriageMilestoneReached)) ) {
            System.out.println("\n========================================\nMILESTONE INFO:");
            if (goldMilestoneReached) System.out.println("- Kamu telah mencapai atau melebihi 17.209g!");
            if (marriageMilestoneReached && player.getPartner() != null) System.out.println("- Kamu telah menikah dengan " + player.getPartner().getName() + "!");
            System.out.println("Permainan akan terus berjalan (infinite gameplay).\nBerikut adalah statistik lengkapmu sejauh ini:\n========================================");
            showStatistics();
        }
    }

    public void showCredits() { 
        System.out.println("\n=== KREDIT ===\nGame Spakbor Hills\nDibuat oleh: Kelompok 03 - IF2010 OOP STI 2024/2025.\nTerima kasih telah bermain!");
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