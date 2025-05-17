package GameCalendar.Controller;

import GameCalendar.Model.GameTime;
import GameCalendar.Model.Season;
import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

public class TimeController {
    private final GameTime gameTime;
    private Timer timer;

    // Konstruktor untuk inisialisasi objek GameTime
    public TimeController(GameTime gameTime) {
        this.gameTime = gameTime;
    }

    // Update waktu dalam game setiap kali dipanggil
    public void updateTime() {
        gameTime.setCurrTime(gameTime.getCurrTime().plusMinutes(5)); // Menambah waktu 5 menit dalam game
        if (gameTime.getCurrTime().equals(LocalTime.MIDNIGHT)) {
            skipToNextDay(); // Melompat ke hari berikutnya ketika jam sudah 00:00
        }
    }

    // Melompat ke hari berikutnya
    public void skipToNextDay() {
        gameTime.setCurrDay(gameTime.getCurrDay() + 1); // Menambah hari
        gameTime.setCurrTime(LocalTime.of(6, 0)); // Set waktu ke jam 06:00
        if (gameTime.getCurrDay() > 10) { // Jika sudah lebih dari 10 hari, ganti musim
            switchSeason();
        }
    }

    // Mengganti musim
    private void switchSeason() {
        Season[] seasons = Season.values();
        int next = (gameTime.getCurrSeason().ordinal() + 1) % seasons.length;
        gameTime.setCurrSeason(seasons[next]); // Mengupdate musim
        gameTime.setCurrDay(1); // Mulai musim baru dari hari pertama
    }

    // Mulai game clock dengan timer
    public void startGameClock() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { 
                updateTime(); // Update waktu tiap detik
            }
        }, 0, 1000); // Mengatur interval per 1000 ms (1 detik)
    }
}
