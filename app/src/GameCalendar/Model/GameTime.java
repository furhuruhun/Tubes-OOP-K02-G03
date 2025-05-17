/* 
 * Command:
 * untuk CHEAT time, season, weather dalam game
 * - pause      : Menjeda waktu dalam game. Waktu tidak akan berjalan.
 * - start      : Melanjutkan waktu game setelah dijeda.
 * - add <menit>: Menambahkan waktu dalam game berdasarkan menit. Contoh: "add 360" akan menambahkan 6 jam (360 menit).
 * - skip <jam> : Lompat ke jam tertentu pada hari berikutnya. Contoh: "skip 15" akan melompat ke jam 15 (3 sore).
 * - show       : Menampilkan waktu, musim, dan cuaca saat ini dalam game.
 * - exit       : Untuk keluar dari aplikasi dan menghentikan game.
*/

package GameCalendar.Model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Interface untuk Observer Pattern
interface WeatherObserver {
    void onWeatherChange(Weather weather);
}

/**
 * GameTime - Menghandle data dan logic untuk sistem GameCalendar
 */
public class GameTime {
    private LocalTime currTime;
    private int currDay;
    private Season currSeason;
    private Weather currWeather;
    private List<WeatherObserver> observers;
    private Random random;
    private int rainyDaysThisSeason;
    private boolean isPaused;

    // Konstruktor
    public GameTime() {
        this.currTime = LocalTime.of(6, 0); // Mulai jam 06:00
        this.currDay = 1;
        this.currSeason = Season.SPRING;
        this.currWeather = Weather.SUNNY;
        this.observers = new ArrayList<>();
        this.random = new Random();
        this.rainyDaysThisSeason = 0;
        this.isPaused = false;
    }

    // Metode untuk update waktu
    public void updateTime() {
        if (isPaused) return;

        // Maju 5 menit (1 detik game time)
        advanceTimeByMinutes(5);

        // Update season/weather jika hari berganti
        if (dayHasChanged()) {
            updateSeason();
            updateWeather();
        }
    }

    // Pause time
    public void pauseTime() {
        isPaused = true;
    }

    // Resume time
    public void startTime() {
        isPaused = false;
    }

    // Add time (dalam menit)
    public void addTime(int minutes) {
        if (minutes < 0) throw new IllegalArgumentException("Menit harus bernilai positif");

        advanceTimeByMinutes(minutes);

        // Update season/weather jika hari berganti
        if (dayHasChanged()) {
            updateSeason();
            updateWeather();
        }
    }

    // Skip ke jam tertentu pada hari berikutnya
    public void timeSkip(int targetHour) {
        if (targetHour < 0 || targetHour > 23) {
            throw new IllegalArgumentException("Target hour harus bernilai 0-23");
        }

        currTime = LocalTime.of(targetHour, 0);
        if (currTime.isBefore(LocalTime.of(6, 0))) {
            currDay++;
            updateSeason();
            updateWeather();
        }
    }

    // Helper method untuk maju sebesar menit tertentu
    private void advanceTimeByMinutes(int minutes) {
        int currentMinutes = currTime.getHour() * 60 + currTime.getMinute();
        currentMinutes += minutes;

        // Menghitung hari yang telah dilewati dan sisa menit
        int daysPassed = currentMinutes / 1440; // 1 hari = 1440 menit
        int remainingMinutes = currentMinutes % 1440;

        // Update day
        currDay += daysPassed;

        // Set new time
        currTime = LocalTime.of(remainingMinutes / 60, remainingMinutes % 60);
    }

    // Helper method untuk cek apakah hari sudah berganti
    private boolean dayHasChanged() {
        return currTime.equals(LocalTime.of(6, 0));
    }

    // Update season (berganti setiap 10 hari)
    private void updateSeason() {
        if (currDay % 10 == 1 && currDay > 1) { // Day 1 of new season
            rainyDaysThisSeason = 0;
            switch (currSeason) {
                case SPRING:
                    currSeason = Season.SUMMER;
                    break;
                case SUMMER:
                    currSeason = Season.FALL;
                    break;
                case FALL:
                    currSeason = Season.WINTER;
                    break;
                case WINTER:
                    currSeason = Season.SPRING;
                    break;
            }
        }
    }

    // Update weather (sekali per day, minimal 2 rainy days per season)
    private void updateWeather() {
        if (currDay % 10 >= 8 && rainyDaysThisSeason < 2) {
            currWeather = Weather.RAINY;
            rainyDaysThisSeason++;
        } else {
            currWeather = (random.nextDouble() < 0.6) ? Weather.SUNNY : Weather.RAINY;
            if (currWeather == Weather.RAINY) {
                rainyDaysThisSeason++;
            }
        }
        notifyObservers();
    }

    // Add observer
    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    // Notify observers when weather changes
    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.onWeatherChange(currWeather);
        }
    }

    // Getters
    public LocalTime getCurrTime() {
        return currTime;
    }

    public int getCurrDay() {
        return currDay;
    }

    public Season getCurrSeason() {
        return currSeason;
    }

    public Weather getCurrWeather() {
        return currWeather;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getRainyDaysThisSeason() {
        return rainyDaysThisSeason;
    }

    public String getPhase() {
        return (currTime.isAfter(LocalTime.of(5, 59)) && currTime.isBefore(LocalTime.of(18, 0))) ? "Siang" : "Malam";
    }

    // Setters
    public void setCurrTime(LocalTime time) {
        this.currTime = time;
    }

    public void setCurrDay(int day) {
        this.currDay = day;
    }

    public void setCurrSeason(Season season) {
        this.currSeason = season;
    }

    public void setCurrWeather(Weather weather) {
        this.currWeather = weather;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public void setRainyDaysThisSeason(int rainyDays) {
        this.rainyDaysThisSeason = rainyDays;
    }
}