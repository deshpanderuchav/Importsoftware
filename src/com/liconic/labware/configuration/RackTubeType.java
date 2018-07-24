package com.liconic.labware.configuration;

public class RackTubeType {

    private int rackTypeID = 0;
    private int rackType = 0;
    private String rackTypeName = "";

    private int tubeTypeID = 0;
    private int tubeType = 0;
    private String tubeTypeName = "";

    public RackTubeType(String tubeTypeName) {
        this.tubeTypeName = tubeTypeName;
    }

    public int getRackTypeID() {
        return rackTypeID;
    }

    public void setRackTypeID(int rackTypeID) {
        this.rackTypeID = rackTypeID;
    }

    public int getRackType() {
        return rackType;
    }

    public void setRackType(int rackType) {
        this.rackType = rackType;
    }

    public String getRackTypeName() {
        return rackTypeName;
    }

    public void setRackTypeName(String rackTypeNAme) {
        this.rackTypeName = rackTypeNAme;
    }

    public int getTubeTypeID() {
        return tubeTypeID;
    }

    public void setTubeTypeID(int tubeTypeID) {
        this.tubeTypeID = tubeTypeID;
    }

    public int getTubeType() {
        return tubeType;
    }

    public void setTubeType(int tubeType) {
        this.tubeType = tubeType;
    }

    public String getTubeTypeName() {
        return tubeTypeName;
    }

    public void setTubeTypeName(String tubeTypeName) {
        this.tubeTypeName = tubeTypeName;
    }

}
