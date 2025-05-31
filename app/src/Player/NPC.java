package Player;
import items.Items;
import java.util.List;

public class NPC {
    private String name;
    private int heartpoints;
    private List<Items> loveditems;
    private List<Items> likeditems;
    private List<Items> hateditems;
    private String relationshipStatus;
    private String location;
    private int dayEngaged;
    private static final int Max_Heartpoint = 150;

    public NPC(String name, int heartpoints, List<Items> loveditems, List<Items> likeditems, List<Items> hateditems, String relationshipStatus, String location, int dayEngaged) {
        this.name = name;
        this.heartpoints = Math.min(heartpoints, Max_Heartpoint);
        this.loveditems = loveditems;
        this.likeditems = likeditems;
        this.hateditems = hateditems;
        this.relationshipStatus = relationshipStatus;
        this.location = location;
        this.dayEngaged = dayEngaged;
    }
    public void setDayEngaged(int day) {
        this.dayEngaged = day;
    }
    public int getDayEngaged() {
        return dayEngaged;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getHeartpoints() {
        return heartpoints;
    }
    public void setHeartpoints(int heartpoints) {
        if(heartpoints > Max_Heartpoint){
            this.heartpoints = Max_Heartpoint;
        }
        else if(heartpoints < 0){
            this.heartpoints = 0;
        }
        else{
            this.heartpoints = heartpoints;
        }
    }
    public List<Items> getLoveditems() {
        return loveditems;
    }
    public void setLoveditems(List<Items> loveditems) {
        this.loveditems = loveditems;
    }
    public List<Items> getLikeditems() {
        return likeditems;
    }
    public void setLikeditems(List<Items> likeditems) {
        this.likeditems = likeditems;
    }
    public List<Items> getHateditems() {
        return hateditems;
    }
    public void setHateditems(List<Items> hateditems) {
        this.hateditems = hateditems;
    }
    public String getRelationshipStatus() {
        return relationshipStatus;
    }
    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
