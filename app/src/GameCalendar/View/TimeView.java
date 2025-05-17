package GameCalendar.View;

import GameCalendar.Model.GameTime;

public class TimeView {
    // Menampilkan status waktu, hari, musim, dan cuaca dalam format yang lebih bervariasi
    public void displayAll(GameTime gameTime) {
        System.out.printf("Waktu: %s | Hari: %d | Musim: %s | Cuaca: %s\n",
            gameTime.getCurrTime(), gameTime.getCurrDay(), gameTime.getCurrSeason(), gameTime.getCurrWeather());
    }

    // Menampilkan pesan peringatan tentang cuaca hujan
    public void showWeatherAlert() {
        System.out.println("[ALERT] Hujan lebat, awas banjir di ladang!");
    }

    // Menampilkan pesan untuk membuka menu cheat yang memungkinkan pengaturan musim dan cuaca manual
    public void showCheatMenu() {
        System.out.println("[CHEAT] Akses pengaturan manual untuk Musim dan Cuaca");
    }
}
