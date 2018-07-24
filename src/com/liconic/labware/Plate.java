package com.liconic.labware;

import com.liconic.hardware.Device;
import com.liconic.hardware.Level;

public class Plate {

    private int ID = 0;
    private String Barcode = "";
    private String UserId = "";
    private Device device = null;
    private Level level = null;
    private int PlateType = 0;
    private String PlateTypeName = "";
    private int Height = 0;

    public int getHeight() {
        return Height;
    }

    public void setHeight(int Height) {
        this.Height = Height;
    }

    public Plate(int ID, String Barcode, String UserId, int PlateType) {
        this.ID = ID;

        if (Barcode == null) {
            this.Barcode = "";
        } else {
            this.Barcode = Barcode;
        }

        if (UserId == null) {
            this.UserId = "";
        } else {
            this.UserId = UserId;
        }

        this.PlateType = PlateType;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getPlateType() {
        return PlateType;
    }

    public void setPlateType(int PlateType) {
        this.PlateType = PlateType;
    }

    public String getPlateTypeName() {
        return PlateTypeName;
    }

    public void setPlateTypeName(String PlateTypeName) {
        this.PlateTypeName = PlateTypeName;
    }

}
