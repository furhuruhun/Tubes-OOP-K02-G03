import GameCalendar.Model.GameTime;
import GameCalendar.View.TimeView;

public class ShowTime extends Action {
    private TimeView timeView;

    public ShowTime(TimeView timeView) {
        super(0, 0);
        this.timeView = timeView;
    }

    @Override
    public void perform(Player player, GameTime gameTime) {
        timeView.displayAll(gameTime);
    }
}
