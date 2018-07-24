package com.liconic.table.exporttasks;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExportPlateModel {

    private final SimpleIntegerProperty idTask;
    private final SimpleIntegerProperty idPlate;
    private final SimpleStringProperty plateBCR;
    private final SimpleStringProperty device;
    private final SimpleStringProperty partition;
    private final SimpleIntegerProperty cassette;
    private final SimpleIntegerProperty level;

    public ExportPlateModel(int idTask, int idPlate, String plateBCR, String device, String partition, int cassette, int level) {

        this.idTask = new SimpleIntegerProperty(idTask);
        this.idPlate = new SimpleIntegerProperty(idPlate);
        this.plateBCR = new SimpleStringProperty(plateBCR);
        this.device = new SimpleStringProperty(device);
        this.partition = new SimpleStringProperty(partition);
        this.cassette = new SimpleIntegerProperty(cassette);
        this.level = new SimpleIntegerProperty(level);

    }

    public int getIdTask() {

        return idTask.get();
    }

    public void setIdTask(int idTask) {

        this.idTask.set(idTask);
    }

    public int getIdPlate() {

        return idPlate.get();
    }

    public void setIdPlate(int idPlate) {

        this.idPlate.set(idPlate);
    }

    public String getPlateBCR() {

        return plateBCR.get();
    }

    public void setPlateBCR(String plateBCR) {

        this.plateBCR.set(plateBCR);
    }

    public String getDevice() {

        return device.get();

    }

    public void setDevice(String device) {

        this.device.set(device);

    }

    public String getPartition() {

        return partition.get();

    }

    public void setPartition(String partition) {

        this.partition.set(partition);

    }

    public int getCassette() {

        return cassette.get();
    }

    public void setCassette(int cassette) {

        this.cassette.set(cassette);
    }

    public int getLevel() {

        return level.get();
    }

    public void setLevel(int level) {

        this.level.set(level);
    }

}
