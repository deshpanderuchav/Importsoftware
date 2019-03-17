package com.liconic.stages;

import com.liconic.binding.stx.STXRequest;
import com.liconic.rest.client.ExportJobClient;
import com.liconic.rest.client.ExportRackClient;

import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Logger;

public class ExportPlateStage extends Stage {

    private final ImportStage importStage;
    private final Logger log;
    private Alert alert;
    private final Scene scene;

    // UI fields
    TextField jobIdField = null;
    TextField plateBCRField = null;
    Button bCancel = null;
    Button bRun = null;

    public ExportPlateStage(ImportStage importStage, Logger log, String WEB_SERVICES_URI) {

        this.importStage = importStage;
        this.log = log;

        initStyle(StageStyle.UTILITY);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));

        jobIdField = new TextField();
        jobIdField.setPromptText("Job Id");
        grid.add(new Label("Job Id:"), 0, 0);
        grid.add(jobIdField, 1, 0);

        plateBCRField = new TextField();
        plateBCRField.setPromptText("Plate Barcode");
        grid.add(new Label("Plate Barcode:"), 0, 1);
        grid.add(plateBCRField, 1, 1);
            
        bCancel = new Button("Cancel");
        grid.add(bCancel, 0, 4);

        bRun = new Button("Run");
        grid.add(bRun, 1, 4);

        bCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
            }
        });

        bRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ExportRackClient client = new ExportRackClient(WEB_SERVICES_URI, log);
                try {
                    String jobId = (null == jobIdField) ? "" : jobIdField.getText();
                    String plateBCR = (null == plateBCRField) ? "" : plateBCRField.getText();

                    STXRequest response = client.runExportRackBC(jobId, plateBCR, importStage.getUser().getLogin());                    
                    
                    String status = "";
                    String errorMsg = "";
                    if ((null != response) && (null != response.getAnswer())) {
                        status = response.getAnswer().getStatus();
                        errorMsg = response.getAnswer().getErrMsg();
                    }

                    if ("OK".equals(status)) {
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Export plate job ran successfully!");
                        log.info("runExportPlate: " + status);
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Export plate job failed! " + errorMsg);
                        log.error("runExportPlate error msg: " + errorMsg);
                    }
                    alert.showAndWait();

                } catch (Exception e) {
                    java.util.logging.Logger.getLogger(ExportPlateStage.class.getName()).log(Level.SEVERE, null, e);
                    
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Export plate job failed! " + e.getMessage());
                    alert.showAndWait();
                    System.out.println("Error: runExportPlate: " + e.getMessage());
                    log.error("runExportPlate: " + e.getMessage());
                    
                } finally {
                    close();
                }
            }
        });

        scene = new Scene(grid, 350, 350);
        setScene(scene);
    }
}
