package com.liconic.table.exporttasks;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExportExceptioRacksModel {

    private final SimpleIntegerProperty idPlate;
    private final SimpleStringProperty plateBCR;
    private final SimpleIntegerProperty cassette;
    private final SimpleIntegerProperty level;

    public ExportExceptioRacksModel(int idPlate, String plateBCR, int cassette, int level) {

        this.idPlate = new SimpleIntegerProperty(idPlate);
        this.plateBCR = new SimpleStringProperty(plateBCR);
        this.cassette = new SimpleIntegerProperty(cassette);
        this.level = new SimpleIntegerProperty(level);

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
