package com.liconic.table.importtasks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class ImportTaskTableModel {

    private final ObjectProperty plate = new SimpleObjectProperty();

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty task;
    private final SimpleStringProperty status;
    private final SimpleStringProperty note;
    private final SimpleIntegerProperty number;

    public ImportTaskTableModel(Object plate, int id, String task, String status, String note, int number) {

        this.plate.set(plate);

        this.id = new SimpleIntegerProperty(id);
        this.task = new SimpleStringProperty(task);
        this.status = new SimpleStringProperty(status);
        this.note = new SimpleStringProperty(note);
        this.number = new SimpleIntegerProperty(number);
    }

    public int getId() {

        return id.get();
    }

    public void setId(int id) {

        this.id.set(id);
    }

    public String getTask() {

        return task.get();
    }

    public void setTask(String task) {

        this.task.set(task);
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

    public Object getPlate() {

        return plate.get();

    }

    public ObjectProperty plateProperty() {

        return plate;

    }

    public int getNumber() {

        return number.get();
    }

    public void setNumber(int number) {

        this.number.set(number);
    }

}
