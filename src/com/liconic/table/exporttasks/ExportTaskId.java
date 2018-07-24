package com.liconic.table.exporttasks;

public class ExportTaskId {

    private int id;
    private String UserName;

    public ExportTaskId(int id, String UserName) {
        this.id = id;
        this.UserName = UserName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

}
