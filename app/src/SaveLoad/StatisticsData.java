package SaveLoad;

import java.util.HashMap;
import java.util.Map;

public class StatisticsData {
    public int totalIncome;
    public int totalExpenditure;
    public int totalDaysPlayed;
    public int totalCropsHarvested;
    public int totalFishCaught;
    public int commonFishCaught;
    public int regularFishCaught;
    public int legendaryFishCaught;
    public Map<String, Integer> chatFrequency;
    public Map<String, Integer> giftFrequency;
    public Map<String, Integer> visitFrequency;
    public boolean goldMilestoneReached;
    public boolean marriageMilestoneReached;

    public StatisticsData() {
        this.chatFrequency = new HashMap<>();
        this.giftFrequency = new HashMap<>();
        this.visitFrequency = new HashMap<>();
    }

    public StatisticsData(int totalIncome, int totalExpenditure, int totalDaysPlayed,
                          int totalCropsHarvested, int totalFishCaught, int commonFishCaught,
                          int regularFishCaught, int legendaryFishCaught,
                          Map<String, Integer> chatFrequency, Map<String, Integer> giftFrequency,
                          Map<String, Integer> visitFrequency, boolean goldMilestoneReached,
                          boolean marriageMilestoneReached) {
        this.totalIncome = totalIncome;
        this.totalExpenditure = totalExpenditure;
        this.totalDaysPlayed = totalDaysPlayed;
        this.totalCropsHarvested = totalCropsHarvested;
        this.totalFishCaught = totalFishCaught;
        this.commonFishCaught = commonFishCaught;
        this.regularFishCaught = regularFishCaught;
        this.legendaryFishCaught = legendaryFishCaught;
        this.chatFrequency = (chatFrequency != null) ? chatFrequency : new HashMap<>();
        this.giftFrequency = (giftFrequency != null) ? giftFrequency : new HashMap<>();
        this.visitFrequency = (visitFrequency != null) ? visitFrequency : new HashMap<>();
        this.goldMilestoneReached = goldMilestoneReached;
        this.marriageMilestoneReached = marriageMilestoneReached;
    }
}