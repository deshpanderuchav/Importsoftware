package com.liconic.table.exporttasks;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ExportTubePickModel {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty srcTubeBC;
    private final SimpleIntegerProperty srcX;
    private final SimpleIntegerProperty srcY;
    private final SimpleStringProperty srcRackBC;

    private final SimpleStringProperty trgRackBC;
    private final SimpleIntegerProperty trgX;
    private final SimpleIntegerProperty trgY;

    private final SimpleStringProperty status;
    private final SimpleStringProperty note;

    public ExportTubePickModel(int id, String srcTubeBC, int srcX, int srcY, String srcRackBC, String trgRackBC, int trgX, int trgY, String status, String note) {

        this.id = new SimpleIntegerProperty(id);

        this.srcTubeBC = new SimpleStringProperty(srcTubeBC);

        this.srcX = new SimpleIntegerProperty(srcX);
        this.srcY = new SimpleIntegerProperty(srcY);

        this.srcRackBC = new SimpleStringProperty(srcRackBC);
        this.trgRackBC = new SimpleStringProperty(trgRackBC);

        this.trgX = new SimpleIntegerProperty(trgX);
        this.trgY = new SimpleIntegerProperty(trgY);

        this.status = new SimpleStringProperty(status);
        this.note = new SimpleStringProperty(note);

    }

    public int getId() {

        return id.get();
    }

    public void setId(int id) {

        this.id.set(id);
    }

    public String getSrcTubeBC() {

        return srcTubeBC.get();
    }

    public void setSrcTubeBC(String srcTubeBC) {

        this.srcTubeBC.set(srcTubeBC);
    }

    public int getSrcX() {

        return srcX.get();
    }

    public void setSrcX(int srcX) {

        this.srcX.set(srcX);
    }

    public int getSrcY() {

        return srcY.get();
    }

    public void setSrcY(int srcY) {

        this.srcY.set(srcY);
    }

    public String getSrcRackBC() {

        return srcRackBC.get();
    }

    public void setSrcRackBC(String srcRackBC) {

        this.srcRackBC.set(srcRackBC);
    }

    public String getTrgRackBC() {

        return trgRackBC.get();
    }

    public void setTrgRackBC(String trgRackBC) {

        this.trgRackBC.set(trgRackBC);
    }

    public int getTrgX() {

        return trgX.get();
    }

    public void setTrgX(int trgX) {

        this.trgX.set(trgX);
    }

    public int getTrgY() {

        return trgY.get();
    }

    public void setTrgY(int trgY) {

        this.trgY.set(trgY);
    }

    public String getStatus() {

        return status.get();

    }

    public void setStatus(String status) {

        this.status.set(status);

    }

    public String getNote() {

        return note.get();

    }

    public void setNote(String note) {

        this.note.set(note);

    }

}
