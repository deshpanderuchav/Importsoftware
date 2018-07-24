package com.liconic.hardware;

import com.liconic.stages.gui.DeviceStatusTab;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Device {

    private int id;
    private String DevId;

    private boolean STT;
    private boolean STB;
    private boolean STC;

    private boolean busy;
    private boolean error;
    private int errorCode;
    private boolean init;
    private boolean door;
    private boolean doorOpened;
    private int climate = -1;
    private DeviceStatusTab statusTab;

    public Device(int id, String DevId) {

        this.id = id;
        this.DevId = DevId;

    }

    public void setSTT(boolean isSTT) {
        this.STT = isSTT;
    }

    public void setSTB(boolean isSTB) {
        this.STB = isSTB;
    }

    public void setSTC(boolean isSTC) {
        this.STC = isSTC;
    }

    public int getId() {
        return id;
    }

    public String getDevId() {
        return DevId;
    }

    public boolean isSTT() {
        return STT;
    }

    public boolean isSTB() {
        return STB;
    }

    public boolean isSTC() {
        return STC;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public boolean isDoor() {
        return door;
    }

    public void setDoor(boolean door) {
        this.door = door;
    }

    public boolean isDoorOpened() {
        return doorOpened;
    }

    public void setDoorOpened(boolean doorOpened) {
        this.doorOpened = doorOpened;
    }
    
    public String getClimate(){
        String request = "STX2GetClimateAlarm(STXBuffer)";
              try {
            int port = 3333;
            String hostname = "localhost";
            String input,output;
            Socket socket = new Socket(hostname, port);           /*Connects to server*/
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(request);       
            BufferedReader in = new BufferedReader(new
            InputStreamReader(socket.getInputStream()));  
            String response = in.readLine();/*Reads from server*/
            System.out.println("Response from server for "+ request + " is "+ response);
            out.close();    /*Closes all*/
            in.close();
            socket.close();
            return response;
      }

        catch(Exception e) {
         System.out.print("Error Connecting to Server\n");
         return "";
        } 
           
    }

    public void setStatusTab(DeviceStatusTab statusTab) {
        this.statusTab = statusTab;
    }

    public void drawStatusGUI() {

        if (statusTab != null) {
            statusTab.drawStatusGUI();
        }
    }

 

}
