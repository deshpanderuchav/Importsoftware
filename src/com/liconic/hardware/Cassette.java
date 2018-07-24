package com.liconic.hardware;

import java.util.ArrayList;
import java.util.List;

public class Cassette {

    private int DBID = 0;
    private int ID = 0;

    private List<Level> levels;

    public Cassette(int DBID, int ID) {

        levels = new ArrayList<>();

        this.DBID = DBID;
        this.ID = ID;
    }

    public int getDBID() {
        return DBID;
    }

    public void setDBID(int DBID) {
        this.DBID = DBID;
    }

    public int getID() {
        return ID;
    }

    public List<Level> getLevels() {
        return levels;
    }

}
