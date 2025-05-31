package farm.World;

import java.util.List;


public class WorldMap {
    private List<String> places;

    public WorldMap() {
        this.places = List.of("Farm", "Forest River", "Mountain Lake", "Ocean", "Store", "Mayor's House", "Caroline's House", "Perry's House", "Dasco's House", "Abigail's House", "Dasco's Gambling Den");
    }

    public void showPlaces() {
        System.out.println("Daftar tempat di World Map: ");
        for(String place: places) {
            System.out.println("- " + place);
        }
    }

        public List<String> getPlaces() {
        return places;
    }

    public boolean isValidLocation(String Place) {
        return places.contains(Place);
    }
}
