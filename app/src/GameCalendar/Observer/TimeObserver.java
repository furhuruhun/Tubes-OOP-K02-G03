package GameCalendar.Observer;

import GameCalendar.Model.GameTime;

public interface TimeObserver {
    /**
     * Dipanggil setiap kali ada update waktu yang signifikan dari GameTime.
     * @param currentTime instance GameTime saat ini.
     */
    void onTimeUpdate(GameTime currentTime);

    /**
     * Dipanggil secara spesifik ketika waktu game mencapai pukul 02:00.
     * @param currentTime instance GameTime saat ini (yang seharusnya menunjukkan 02:00).
     */
    void onTwoAM(GameTime currentTime);
}