package com.liconic.table.importtasks;

import javafx.beans.property.SimpleStringProperty;

public class TaskHistoryModel {

    private final SimpleStringProperty status;
    private final SimpleStringProperty date;
    private final SimpleStringProperty user;
    private final SimpleStringProperty note;

    public TaskHistoryModel(String status, String date, String user, String note) {
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
        this.user = new SimpleStringProperty(user);
        this.note = new SimpleStringProperty(note);
    }

    // Status
    public String getStatus() {

        return status.get();
    }

    public void setStatus(String status) {

        this.status.set(status);
    }

    //date    
    public String getDate() {

        return date.get();
    }

    public void setDate(String date) {

        this.date.set(date);
    }

    // user
    public String getUser() {

        return user.get();
    }

    public void setUser(String user) {

        this.user.set(user);
    }

    // note
    public String getNote() {

        return note.get();
    }

    public void setNote(String note) {

        this.note.set(note);
    }

}
