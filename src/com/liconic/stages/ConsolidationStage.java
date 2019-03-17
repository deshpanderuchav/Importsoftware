package com.liconic.stages;

import com.liconic.rest.client.ConsolidationClient;

import java.io.File;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Logger;

public class ConsolidationStage extends Stage {

    public static final String CONSOLIDATION_TYPE_BY_PLATES = "By Plates";
    public static final String CONSOLIDATION_TYPE_BY_PARTITION = "By Partition";
    public static final String CONSOLIDATION_TYPE_REFORMATTING = "Reformatting";

    private final ImportStage importStage;
    private final String consolidationType;
    private final Logger log;
    private final Scene scene;
    private final FileChooser platesFileChooser = new FileChooser();
    private File platesFile;
    int rowIndex = 0;

    // UI fields
    TextField jobIdField = null;
    TextField partitionField = null;
    TextField sourcePartitionField = null;
    TextField targetPartitionField = null;
    TextField tubePickerField = null;
    TextField sourceTypeField = null;
    TextField targetTypeField = null;
    Button bChoosePlatesFile = null;
    Label selectedFileName = null;
    Button bCancel = null;
    Button bRun = null;

    public ConsolidationStage(ImportStage importStage, Logger log, String WEB_SERVICES_URI, String consolidationType) {

        this.importStage = importStage;
        this.log = log;
        this.consolidationType = consolidationType;

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
        partitionField.setPromptText("Partition");
        grid.add(new Label("Partition:"), 0, rowIndex);
        grid.add(partitionField, 1, rowIndex);
        rowIndex++;

        if (CONSOLIDATION_TYPE_REFORMATTING.equals(consolidationType)) {
            sourcePartitionField = new TextField();
            sourcePartitionField.setPromptText("Source Partition");
            grid.add(new Label("Source Partition:"), 0, rowIndex);
            grid.add(sourcePartitionField, 1, rowIndex);
            rowIndex++;

            targetPartitionField = new TextField();
            targetPartitionField.setPromptText("Target Partition");
            grid.add(new Label("Target Partition:"), 0, rowIndex);
            grid.add(targetPartitionField, 1, rowIndex);
            rowIndex++;

            tubePickerField = new TextField();
            tubePickerField.setPromptText("Tube Picker");
            grid.add(new Label("Tube Picker:"), 0, rowIndex);
            grid.add(tubePickerField, 1, rowIndex);
            rowIndex++;

            sourceTypeField = new TextField();
            sourceTypeField.setPromptText("Source Type");
            grid.add(new Label("Source Type:"), 0, rowIndex);
            grid.add(sourceTypeField, 1, rowIndex);
            rowIndex++;

            targetTypeField = new TextField();
            targetTypeField.setPromptText("Target Type");
            grid.add(new Label("Target Type:"), 0, rowIndex);
            grid.add(targetTypeField, 1, rowIndex);
            rowIndex++;
        }

        if (CONSOLIDATION_TYPE_BY_PLATES.equals(consolidationType)) {
            grid.add(new Label("Plates:"), 0, rowIndex);
            bChoosePlatesFile = new Button("Choose file");
            grid.add(bChoosePlatesFile, 1, rowIndex);
            rowIndex++;
            final int rowIdx = rowIndex;

            bChoosePlatesFile.setOnAction(
                    new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    platesFile = platesFileChooser.showOpenDialog(scene.getWindow());
                    grid.getChildren().remove(selectedFileName);
                    selectedFileName = new Label(platesFile.getName());
                    grid.add(selectedFileName, 1, rowIdx);
                }
            });
        }

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
                ConsolidationClient client = new ConsolidationClient(WEB_SERVICES_URI, log);
                try {
                    String jobId = (null == jobIdField) ? "" : jobIdField.getText();
                    String partition = (null == partitionField) ? "" : partitionField.getText();
                    String targetPartition = (null == targetPartitionField) ? "" : targetPartitionField.getText();
                    String sourcePartition = (null == sourcePartitionField) ? "" : sourcePartitionField.getText();
                    String tubePicker = (null == tubePickerField) ? "" : tubePickerField.getText();
                    String sourceType = (null == sourceTypeField) ? "" : sourceTypeField.getText();
                    String targetType = (null == targetTypeField) ? "" : targetTypeField.getText();

                    client.setConsolidation(importStage.getUser().getLogin(), getConsolidationType(), jobId, partition,
                            sourcePartition, targetPartition, tubePicker, sourceType, targetType, platesFile);
                    close();

                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(ConsolidationStage.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        });

        scene = new Scene(grid, 600, 600);
        setScene(scene);
    }

    public String getConsolidationType() {
        return consolidationType;
    }
}
