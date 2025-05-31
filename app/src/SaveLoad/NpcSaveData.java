package SaveLoad;

public class NpcSaveData { 
    public String name;
    public int heartpoints;
    public String relationshipStatus;
    public int dayEngaged;

    public NpcSaveData() {}

    public NpcSaveData(String name, int heartpoints, String relationshipStatus, int dayEngaged) {
        this.name = name;
        this.heartpoints = heartpoints;
        this.relationshipStatus = relationshipStatus;
        this.dayEngaged = dayEngaged;
    }
}