package com.liconic.table.importtasks;

import javafx.beans.property.SimpleStringProperty;

public class TaskPropertyModel {

    private final SimpleStringProperty property;
    private final SimpleStringProperty value;

    public TaskPropertyModel(String property, String value) {
        this.property = new SimpleStringProperty(property);
        this.value = new SimpleStringProperty(value);
    }

    // Property
    public String getProperty() {

        return property.get();
    }

    public void setProperty(String property) {

        this.property.set(property);
    }

    // Value    
    public String getValue() {

        return value.get();
    }

    public void setValue(String value) {

        this.value.set(value);
    }

}
