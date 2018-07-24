package com.liconic.table.settings;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class SettingsTableRecord {

    private final SimpleIntegerProperty id;
    private final SimpleStringProperty paramName;
    private final SimpleStringProperty paramValue;

    public SettingsTableRecord(int id, String paramName, String paramValue) {

        this.id = new SimpleIntegerProperty(id);
        this.paramName = new SimpleStringProperty(paramName);
        this.paramValue = new SimpleStringProperty(paramValue);

    }

    public int getId() {

        return id.get();
    }

    public void setId(int id) {

        this.id.set(id);
    }

    public String getParamName() {

        return paramName.get();
    }

    public void setParamName(String paramName) {

        this.paramName.set(paramName);
    }

    public String getParamValue() {

        return paramValue.get();

    }

    public void setParamValue(String paramValue) {

        this.paramValue.set(paramValue);

    }

}
