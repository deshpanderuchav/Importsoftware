/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.stages;

import com.liconic.rest.client.ImportJobClient;

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

/**
 *
 * @author Rucha Deshpande
 */
class ImportxferStage extends Stage {
    
    public static final String XFER = "Through Xfer Station";
    public static final String PRELOAD = "PRELOAD";

    private final ImportStage importStage;
    private final String importType;
    private final Logger log;
    private final Scene scene;
    private final FileChooser samplesFileChooser = new FileChooser();
    private File samplesFile;
    int rowIndex = 0;

    // UI fields
    TextField jobIdField = null;
    TextField partitionField = null;
    TextField plateBarcodeField = null;
    TextField Type = null;
    TextField tubePickerField = null;
    TextField targetTypeField = null;
    CheckBox scanField = null;
    Button bChooseSamplesFile = null;
    Label selectedFileName = null;
    Button bCancel = null;
    Button bRun = null;
  

    public ImportxferStage(ImportStage importStage, Logger log, String WEB_SERVICES_URI, String importType) {

        this.importStage = importStage;
        this.log = log;
        this.importType = importType;

        initStyle(StageStyle.UTILITY);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));

        jobIdField = new TextField();
        jobIdField.setPromptText("Job Id");
        grid.add(new Label("Job Id:"), 0, rowIndex);
        grid.add(jobIdField, 1, rowIndex);
        rowIndex++;
               
            partitionField = new TextField();
            partitionField.setPromptText("Target Partition");
            grid.add(new Label("Target Partition:"), 0, rowIndex);
            grid.add(partitionField, 1, rowIndex);
            rowIndex++;

            plateBarcodeField = new TextField();
            plateBarcodeField.setPromptText("Plate Barcode");
            grid.add(new Label("Plate Barcode:"), 0, rowIndex);
            grid.add(plateBarcodeField, 1, rowIndex);
            rowIndex++;
            
           Type = new TextField();
            Type.setPromptText("Plate Type");
            grid.add(new Label("Plate Type:"), 0, rowIndex);
            grid.add(Type, 1, rowIndex);
            rowIndex++;
            
          scanField = new CheckBox();
        grid.add(new Label("Empty:"), 0, rowIndex);
        grid.add(scanField, 1, rowIndex);
        rowIndex++;

        final int rowIdx = rowIndex;

        
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
                ImportJobClient client = new ImportJobClient(WEB_SERVICES_URI, log);
                try {
                    String jobId = (null == jobIdField) ? "" : jobIdField.getText();
                    String partition = (null == partitionField) ? "" : partitionField.getText();
                    String plateBarcode = (null == plateBarcodeField) ? "" : plateBarcodeField.getText();
                    
                    String type = (null == Type) ? "" : Type.getText();
//                    String tubePicker = (null == tubePickerField) ? "" : tubePickerField.getText();
//                    String targetType = (null == targetTypeField) ? "" : targetTypeField.getText();
                    boolean empty = (null == scanField) ? false : scanField.isSelected();
                    client.setImport(importStage.getUser().getLogin(), getImportType(), jobId, partition,
                            plateBarcode,empty,type);
                    close();

                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(ImportxferStage.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        });

        scene = new Scene(grid, 500, 500);
        setScene(scene);
    }

    public String getImportType() {
        return importType;
    }
}


