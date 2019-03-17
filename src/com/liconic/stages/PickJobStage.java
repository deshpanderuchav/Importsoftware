package com.liconic.stages;

import com.liconic.rest.client.PickJobClient;

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

public class PickJobStage extends Stage {

    public static final String PICK_JOB_TYPE_SIMPLE = "Simple";
    public static final String PICK_JOB_TYPE_WITH_CUSTOM_TUBE_PICKER = "With Custom Tube Picker";

    private final ImportStage importStage;
    private final String pickJobType;
    private final Logger log;
    private final Scene scene;
    private final FileChooser samplesFileChooser = new FileChooser();
    private File samplesFile;
    int rowIndex = 0;

    // UI fields
    TextField jobIdField = null;
    TextField partitionField = null;
    TextField targetPartitionField = null;
    TextField tubePickerField = null;
    TextField targetTypeField = null;
    Button bChooseSamplesFile = null;
    Label selectedFileName = null;
    Button bCancel = null;
    Button bRun = null;

    public PickJobStage(ImportStage importStage, Logger log, String WEB_SERVICES_URI, String pickJobType) {

        this.importStage = importStage;
        this.log = log;
        this.pickJobType = pickJobType;

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

        if (PICK_JOB_TYPE_WITH_CUSTOM_TUBE_PICKER.equals(pickJobType)) {
            partitionField = new TextField();
            partitionField.setPromptText("Partition");
            grid.add(new Label("Partition:"), 0, rowIndex);
            grid.add(partitionField, 1, rowIndex);
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

            targetTypeField = new TextField();
            targetTypeField.setPromptText("Target Type");
            grid.add(new Label("Target Type:"), 0, rowIndex);
            grid.add(targetTypeField, 1, rowIndex);
            rowIndex++;
        }

        grid.add(new Label("Samples:"), 0, rowIndex);
        bChooseSamplesFile = new Button("Choose file");
        grid.add(bChooseSamplesFile, 1, rowIndex);
        rowIndex++;
        final int rowIdx = rowIndex;

        bChooseSamplesFile.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                samplesFile = samplesFileChooser.showOpenDialog(scene.getWindow());
                grid.getChildren().remove(selectedFileName);
                selectedFileName = new Label(samplesFile.getName());
                grid.add(selectedFileName, 1, rowIdx);
            }
        });

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
                PickJobClient client = new PickJobClient(WEB_SERVICES_URI, log);
                try {
                    String jobId = (null == jobIdField) ? "" : jobIdField.getText();
                    String partition = (null == partitionField) ? "" : partitionField.getText();
                    String targetPartition = (null == targetPartitionField) ? "" : targetPartitionField.getText();
                    String tubePicker = (null == tubePickerField) ? "" : tubePickerField.getText();
                    String targetType = (null == targetTypeField) ? "" : targetTypeField.getText();

                    client.setPickJob(importStage.getUser().getLogin(), getPickJobType(), jobId, partition,
                            targetPartition, tubePicker, targetType, samplesFile);
                    close();

                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(PickJobStage.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        });

        scene = new Scene(grid, 500, 500);
        setScene(scene);
    }

    public String getPickJobType() {
        return pickJobType;
    }
}
