public class NPC {
    private String name;
    private int heartpoints;
    private Items loveditems;
    private Items likeditems;
    private Items hateditems;
    private String relationshipStatus;
    private String location;

    public NPC(String name, int heartpoints, Items loveditems, Items likeditems, Items hateditems, String relationshipStatus, String location) {
        this.name = name;
        this.heartpoints = heartpoints;
        this.loveditems = loveditems;
        this.likeditems = likeditems;
        this.hateditems = hateditems;
        this.relationshipStatus = relationshipStatus;
        this.location = location;
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
        this.heartpoints = heartpoints;
    }
    public Items getLoveditems() {
        return loveditems;
    }
    public void setLoveditems(Items loveditems) {
        this.loveditems = loveditems;
    }
    public Items getLikeditems() {
        return likeditems;
    }
    public void setLikeditems(Items likeditems) {
        this.likeditems = likeditems;
    }
    public Items getHateditems() {
        return hateditems;
    }
    public void setHateditems(Items hateditems) {
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
