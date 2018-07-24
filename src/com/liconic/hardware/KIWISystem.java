package com.liconic.hardware;

import java.util.ArrayList;
import java.util.List;

public class KIWISystem {

    private int id;
    private String SysId;

    private List<Device> Devices;

    public KIWISystem(int id, String SysId) {
        this.id = id;
        this.SysId = SysId;
        Devices = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getSysId() {
        return SysId;
    }

    public List<Device> getDevices() {
        return Devices;
    }

    public void AddDevice(Device device) {
        Devices.add(device);
    }

    public String toString() {
        return SysId;
    }
}
