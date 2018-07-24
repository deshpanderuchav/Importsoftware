package com.liconic.hardware;

import com.liconic.labware.Plate;

public class Level {

    private int ID = 0;
    private int LevelId = 0;
    private int Cassette = 0;
    private Device device = null;
    private int LevelType = 0;
    private Plate plate = null;
    private int Height = 0;

    public Level(int ID, int LevelId, int Cassette, int LevelType) {
        this.ID = ID;
        this.LevelId = LevelId;
        this.Cassette = Cassette;
        this.LevelType = LevelType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLevelId() {
        return LevelId;
    }

    public void setLevelId(int LevelId) {
        this.LevelId = LevelId;
    }

    public int getCassette() {
        return Cassette;
    }

    public void setCassette(int Cassette) {
        this.Cassette = Cassette;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public int getLevelType() {
        return LevelType;
    }

    public void setLevelType(int LevelType) {
        this.LevelType = LevelType;
    }

    public Plate getPlate() {
        return plate;
    }

    public void setPlate(Plate plate) {
        this.plate = plate;
        plate.setLevel(this);
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int Height) {
        this.Height = Height;
    }

}
