package com.liconic.buffergui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class BufferModel {

    private final ObjectProperty position = new SimpleObjectProperty();

    private final SimpleStringProperty note;

    public BufferModel(Object position, String note) {

        this.position.set(position);
        this.note = new SimpleStringProperty(note);

    }

    public String getNote() {

        return note.get();

    }

    public void setNote(String note) {

        this.note.set(note);

    }

    public Object getPosition() {

        return position.get();

    }

    public ObjectProperty positionProperty() {

        return position;

    }

}
