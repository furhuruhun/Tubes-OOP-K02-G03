package items;
import java.util.*;
import GameCalendar.Model.Season;
import GameCalendar.Model.Weather;

public class FishDatabase {
    private static List<Fish> allFish = new ArrayList<>();

    static {
        allFish.add(new Fish("Carp", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Mountain Lake", "Farm"}, "Common", 20, 0, 24));
        allFish.add(new Fish("Bullhead", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Mountain Lake"}, "Common", 40, 0, 24));
        allFish.add(new Fish("Chub", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Forest River", "Mountain Lake"}, "Common", 20, 0, 24));

        allFish.add(new Fish("Largemouth Bass", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Mountain Lake"}, "Regular", 40, 6, 18));
        allFish.add(new Fish("Salmon", new Season[]{Season.FALL}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Forest River"}, "Regular", 160, 6, 18));
        allFish.add(new Fish("Pufferfish", new Season[]{Season.SUMMER}, new Weather[]{Weather.SUNNY}, new String[]{"Ocean"}, "Regular", 240, 0, 16));
        allFish.add(new Fish("Halibut", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Ocean"}, "Regular", 60, 6, 11));
        allFish.add(new Fish("Catfish", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL}, new Weather[]{Weather.RAINY}, new String[]{"Forest River", "Farm"}, "Regular", 40, 6, 22));
        allFish.add(new Fish("Sturgeon", new Season[]{Season.SUMMER, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Mountain Lake"}, "Regular", 80, 6, 18));
        allFish.add(new Fish("Rainbow Trout", new Season[]{Season.SUMMER}, new Weather[]{Weather.SUNNY}, new String[]{"Forest River", "Mountain Lake"}, "Regular", 160, 6, 18));
        allFish.add(new Fish("Sardine", new Season[]{Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Ocean"}, "Regular", 40, 6, 18));
        allFish.add(new Fish("Midnight Carp", new Season[]{Season.WINTER, Season.FALL}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Mountain Lake", "Farm"}, "Regular", 80, 20, 2));
        allFish.add(new Fish("Flounder", new Season[]{Season.SPRING, Season.SUMMER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Ocean"}, "Regular", 60, 6, 22));
        allFish.add(new Fish("Octopus", new Season[]{Season.SUMMER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Ocean"}, "Regular", 120, 6, 22));
        allFish.add(new Fish("Super Cucumber", new Season[]{Season.SUMMER, Season.FALL, Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Ocean"}, "Regular", 80, 18, 2));

        allFish.add(new Fish("Legend", new Season[]{Season.SPRING}, new Weather[]{Weather.RAINY}, new String[]{"Mountain Lake"}, "Legendary", 1600, 8, 20));
        allFish.add(new Fish("Angler", new Season[]{Season.FALL}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Farm"}, "Legendary", 800, 8, 20));
        allFish.add(new Fish("Crimsonfish", new Season[]{Season.SUMMER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Ocean"}, "Legendary", 800, 8, 20));
        allFish.add(new Fish("Glacierfish", new Season[]{Season.WINTER}, new Weather[]{Weather.SUNNY, Weather.RAINY}, new String[]{"Forest River"}, "Legendary", 800, 8, 20));
    }
    public static List<Fish> getFishFor(String location, Season season, int time, Weather weather) {
        List<Fish> result = new ArrayList<>();

        // System.out.println("DEBUG FishDB - Params Diterima: Lokasi=" + location + 
        //                 ", Musim=" + season + ", Jam=" + time + ", Cuaca=" + weather); // BARIS INI PENTING

        for (Fish fish : allFish) {
            boolean matchLoc = Arrays.asList(fish.getLocation()).contains(location);
            boolean matchSeason = Arrays.asList(fish.getSeason()).contains(Season.ANY) || Arrays.asList(fish.getSeason()).contains(season);
            boolean matchWeather = Arrays.asList(fish.getWeather()).contains(Weather.ANY) || Arrays.asList(fish.getWeather()).contains(weather);
            int[] range = fish.getTime();
            boolean matchTime;
            if (range[0] == 0 && range[1] == 24) { // Sepanjang hari, jam 00:00 s/d 23:59
                matchTime = (time >= 0 && time <= 23); // Jam adalah 0-23
            } else if (range[0] <= range[1]) { // Rentang normal, misal 6-18
                matchTime = (time >= range[0] && time <= range[1]);
            } else { // Rentang melewati tengah malam, misal 20-2
                matchTime = (time >= range[0] || time <= range[1]);
            }

            // // BARIS DEBUGGING PER IKAN INI SANGAT PENTING:
            // System.out.println("DEBUG FishDB - Mengecek Ikan: " + fish.getName() + 
            //                 " | Data Ikan: Lok[" + String.join(",", fish.getLocation()) +"] Mus[" + Arrays.toString(fish.getSeason()) + "] Jam[" + range[0] + "-" + range[1] + "] Cua[" + Arrays.toString(fish.getWeather()) + "]" +
            //                 " | Cocok? Lok=" + matchLoc + ", Mus=" + matchSeason + ", Jam=" + matchTime + ", Cua=" + matchWeather);

            if (matchLoc && matchSeason && matchWeather && matchTime) {
                result.add(fish);
                // System.out.println("    ==> IKAN DITEMUKAN & DITAMBAHKAN: " + fish.getName()); // BARIS INI PENTING
            }
        }
        // System.out.println("DEBUG FishDB - Total ikan ditemukan di akhir: " + result.size()); // BARIS INI PENTING
        return result;
    }

    public static List<Fish> getAllFish() {
        return allFish;
    }
}