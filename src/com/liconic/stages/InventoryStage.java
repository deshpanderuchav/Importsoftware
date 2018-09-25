package com.liconic.stages;

import com.liconic.rest.client.InventoryClient;

import java.io.File;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Logger;

public class InventoryStage extends Stage {

    public static final String INVENTORY_TYPE_PARTITION = "Partition";
    public static final String INVENTORY_TYPE_DEFINED_CASSETTES = "Defined Cassettes";
    public static final String INVENTORY_TYPE_DEFINED_PLATE = "Defined Plate";

    private final ImportStage importStage;
    private final String inventoryType;
    private final Logger log;
    private final Scene scene;
    private final FileChooser plateFileChooser = new FileChooser();
    private File plateFile;

    // UI fields
    TextField jobIdField = null;
    TextField partitionField = null;
    TextField firstCassetteField = null;
    TextField lastCassetteField = null;
    TextField deviceField = null;
    Button bChooseFile = null;
    CheckBox scanField = null;
    Button bCancel = null;
    Button bRun = null;

    public InventoryStage(ImportStage importStage, Logger log, String WEB_SERVICES_URI, String inventoryType) {

        this.importStage = importStage;
        this.log = log;
        this.inventoryType = inventoryType;

        initStyle(StageStyle.UTILITY);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));

        int rowIndex = 0;

        jobIdField = new TextField();
        jobIdField.setPromptText("Job Id");
        grid.add(new Label("Job Id:"), 0, rowIndex);
        grid.add(jobIdField, 1, rowIndex);
        rowIndex++;

        if (INVENTORY_TYPE_PARTITION.equals(inventoryType)) {
            partitionField = new TextField();
            partitionField.setPromptText("Partition");
            grid.add(new Label("Partition:"), 0, rowIndex);
            grid.add(partitionField, 1, rowIndex);
            rowIndex++;
        }

        if (INVENTORY_TYPE_DEFINED_CASSETTES.equals(inventoryType)) {
            deviceField = new TextField();
            deviceField.setPromptText("Device");
            grid.add(new Label("Device:"), 0, rowIndex);
            grid.add(deviceField, 1, rowIndex);
            rowIndex++;

            firstCassetteField = new TextField();
            firstCassetteField.setPromptText("First Cassette");
            grid.add(new Label("First Cassette:"), 0, rowIndex);
            grid.add(firstCassetteField, 1, rowIndex);
            rowIndex++;

            lastCassetteField = new TextField();
            lastCassetteField.setPromptText("Last Cassette");
            grid.add(new Label("Last Cassette:"), 0, rowIndex);
            grid.add(lastCassetteField, 1, rowIndex);
            rowIndex++;
        }

        if (INVENTORY_TYPE_DEFINED_PLATE.equals(inventoryType)) {
            grid.add(new Label("Plate:"), 0, rowIndex);
            bChooseFile = new Button("Choose file");
            grid.add(bChooseFile, 1, rowIndex);
            rowIndex++;

            bChooseFile.setOnAction(
                    new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    plateFile = plateFileChooser.showOpenDialog(scene.getWindow());
                }
            });
        }

        scanField = new CheckBox();
        grid.add(new Label("2D Scan:"), 0, rowIndex);
        grid.add(scanField, 1, rowIndex);
        rowIndex += 3;

        bCancel = new Button("Cancel");
        grid.add(bCancel, 0, rowIndex);

        bRun = new Button("Run");
        grid.add(bRun, 1, rowIndex);

        bCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
            }
        });

        bRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                InventoryClient client = new InventoryClient(WEB_SERVICES_URI, log);
                try {
                    String jobId = (null == jobIdField) ? "" : jobIdField.getText();
                    String partition = (null == partitionField) ? "" : partitionField.getText();
                    int firstCassette = (null == firstCassetteField) ? 0 : Integer.parseInt(firstCassetteField.getText());
                    int lastCassette = (null == lastCassetteField) ? 0 : Integer.parseInt(lastCassetteField.getText());
                    String device = (null == deviceField) ? "" : deviceField.getText();
                    boolean scan = (null == scanField) ? false : scanField.isSelected();

                    client.setInventory(importStage.getUser().getLogin(), getInventoryType(), jobId, partition,
                            firstCassette, lastCassette, device, scan, plateFile);
                    close();

                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(InventoryStage.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        });

        scene = new Scene(grid, 300, 300);
        setScene(scene);
    }

    public String getInventoryType() {
        return inventoryType;
    }
}
