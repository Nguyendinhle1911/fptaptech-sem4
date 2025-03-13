package model;

public class Player {
    private int playerId;
    private String name;
    private String fullName;
    private String age;
    private int indexId;

    // Getters and Setters
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
    public int getIndexId() { return indexId; }
    public void setIndexId(int indexId) { this.indexId = indexId; }
}