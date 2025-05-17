public abstract class Action {
    protected int energycost;
    protected int timeCostInMinute;

    public Action(int energycost, int timeCostInMinute) {
        this.energycost = energycost;
        this.timeCostInMinute = timeCostInMinute;
    }

    public abstract void perform(Player player, GameTime gameTime);
}
