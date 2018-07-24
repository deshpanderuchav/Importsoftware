package com.liconic.stages.gui;

import com.liconic.hardware.Device;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class DeviceStatusTab extends HBox {

    private ImageView statusImg;
    private Label labelStatus;
    private Device dev;
    private String DevId;

    private Image grayImg = null;
    private Image redImg = null;
    private Image yellowImg = null;
    private Image greenImg = null;

    public DeviceStatusTab(Device dev) {

        this.dev = dev;

        DevId = dev.getDevId();

        grayImg = new Image("images/ledgray.png");
        redImg = new Image("images/ledred.png");
        yellowImg = new Image("images/ledyellow.png");
        greenImg = new Image("images/ledgreen.png");

        statusImg = new ImageView(grayImg);
        labelStatus = new Label(DevId);

        this.setAlignment(Pos.CENTER_LEFT);
        this.setMinHeight(20);
        this.setPrefHeight(20);

        this.setSpacing(5);

        this.setMinWidth(180);

        dev.setStatusTab(this);

        getChildren().addAll(statusImg, labelStatus);

    }

    public void drawStatusGUI() {

        if (dev.isInit()) {

            if (dev.isError()) {

                if (dev.isDoor()) {

                    if (dev.isDoorOpened()) {
                        labelStatus.setText(DevId + " (error, door opened)");
                    }
                }

                statusImg.setImage(redImg);
                labelStatus.setText(DevId + " (error)");

            } else if (dev.isBusy()) {

                if (dev.isDoor()) {

                    if (dev.isDoorOpened()) {
                        statusImg.setImage(redImg);
                        labelStatus.setText(DevId + " (busy, door opened)");
                    } else {
                        statusImg.setImage(yellowImg);
                        labelStatus.setText(DevId + " (busy)");
                    }

                } else {
                    statusImg.setImage(yellowImg);
                    labelStatus.setText(DevId + " (busy)");
                }

            } else if (dev.isDoor()) {

                if (dev.isDoorOpened()) {
                    statusImg.setImage(redImg);
                    labelStatus.setText(DevId + " (ready, door opened)");
                } else {
                    statusImg.setImage(greenImg);
                    labelStatus.setText(DevId + " (ready)");
                }

            } else {
                statusImg.setImage(greenImg);
                labelStatus.setText(DevId + " (ready)");
                    if(dev.isSTB()){
            if(dev.getClimate().equals("0")){
                labelStatus.setText(DevId + "(ready)");
            }
            else if(dev.getClimate().equals("1")){
                statusImg.setImage(grayImg);
                 labelStatus.setText(DevId + "(Temprature is out of range)");
            }
             else if(dev.getClimate().equals("2")){
                 statusImg.setImage(grayImg);
                 labelStatus.setText(DevId + "(Humidity is out of range)");
            }
             else if(dev.getClimate().equals("3")){
                 statusImg.setImage(grayImg);
                 labelStatus.setText(DevId + "(Temperature and Humidity both are out of range)");
            }
            else
             {
                 
             }
        }
                    
            }

        } else {
            statusImg.setImage(grayImg);
            labelStatus.setText(DevId + " (not initialized)");
        }

        
        
   
    }

}
