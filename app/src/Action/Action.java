package Action;

import Player.Player;
import GameCalendar.Model.GameTime;

public abstract class Action {
    protected int energyCost;
    protected int timeCostInMinute;

    public Action(int energyCost, int timeCostInMinute) {
        this.energyCost = energyCost;
        this.timeCostInMinute = timeCostInMinute;
    }

    public abstract void perform(Player player, GameTime gametime);

    public int getEnergyCost() {
        return energyCost;
    }

    public int getTimeCostInMinute() {
        return timeCostInMinute;
    }
    public void setEnergyCost(int energyCost) {
        this.energyCost = energyCost;
    }

    public void setTimeCostInMinute(int timeCostInMinute) {
        this.timeCostInMinute = timeCostInMinute;
    }
}