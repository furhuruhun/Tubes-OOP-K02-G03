package GameCalendar.Model;

public class ManageWeather {
    private int rainyDaysThisSeason = 0;

    public int getRainyDaysThisSeason() { 
        return rainyDaysThisSeason; 
    }

    public void incrementRainyDays() { 
        rainyDaysThisSeason++; 
    }
    
    public void resetRainyDays() { 
        rainyDaysThisSeason = 0; 
    }
}