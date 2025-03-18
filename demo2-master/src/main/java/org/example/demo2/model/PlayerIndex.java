package org.example.demo2.model;

public class PlayerIndex {
    private int id;
    private int playerId;
    private int indexId;
    private float value;

    public PlayerIndex() {}
    public PlayerIndex(int playerId, int indexId, float value) {
        this.playerId = playerId;
        this.indexId = indexId;
        this.value = value;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }
    public int getIndexId() { return indexId; }
    public void setIndexId(int indexId) { this.indexId = indexId; }
    public float getValue() { return value; }
    public void setValue(float value) { this.value = value; }
}
