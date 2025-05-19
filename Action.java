public abstract class Action {
    protected int energyCost;
    protected int timeCostInMinute;

    public Action(int energyCost, int timeCostInMinute) {
        this.energyCost = energyCost;
        this.timeCostInMinute = timeCostInMinute;
    }

    public abstract void perform(Player player);
}
