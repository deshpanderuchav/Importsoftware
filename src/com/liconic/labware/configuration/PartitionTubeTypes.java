package com.liconic.labware.configuration;

import java.util.ArrayList;
import java.util.List;

public class PartitionTubeTypes {

    private String partitionName;
    private List<RackTubeType> rackTubesTypes;

    public PartitionTubeTypes(String partitionName) {
        this.partitionName = partitionName;
        rackTubesTypes = new ArrayList<>();
    }

    public String getPartitionName() {
        return partitionName;
    }

    public List<RackTubeType> getTubesTypes() {
        return rackTubesTypes;
    }

}
