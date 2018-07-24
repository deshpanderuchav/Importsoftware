package com.liconic.table.exporttasks;

import java.util.ArrayList;
import java.util.List;

public class ExportPickList {

    private List<ExportTubePickModel> pickList;

    public ExportPickList() {
        pickList = new ArrayList<>();
    }

    public List<ExportTubePickModel> getPickList() {
        return pickList;
    }

    public void setPickLine(ExportTubePickModel picLine) {
        pickList.add(picLine);
    }

}
