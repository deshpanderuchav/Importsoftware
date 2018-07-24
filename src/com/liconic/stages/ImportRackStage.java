package com.liconic.stages;

import com.liconic.db.DM;
import com.liconic.labware.configuration.PartitionTubeTypes;
import com.liconic.labware.configuration.RackTubeType;
import com.liconic.user.User;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.Logger;

public class ImportRackStage extends Stage {

    private Scene scene;
    private BorderPane root;
    private Image image;
    private ImageView imageView;
    private VBox vboxMain;
    private VBox vboxKiwi;

    private HBox hbBarcode;
    private Label lbBarcode;
    private Label lbRackBarcode;
    private TextField tfRackBarcode;

    private HBox hbLabware;
    private Label lbLabwareType;
    private ChoiceBox cbLabware;

    private HBox hbEmpty;
    private CheckBox cbEmpty;
    private DM dm;
    private User user;
    private String Barcode;
    private Logger log;

    private ImportStage importStage;

    private List<PartitionTubeTypes> PartitionTubeTypesList;

    public ImportRackStage(final ImportStage importStage, User user, Logger log, boolean isBCR) {

        this.importStage = importStage;
        this.user = user;
        this.log = log;

        log.info("Open ImportRack window");

        dm = importStage.getDm();

        initStyle(StageStyle.UTILITY);

        root = new BorderPane();

        image = new Image("images/barcode-icon.png");

        imageView = new ImageView();
        imageView.setImage(image);

        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        VBox vbImage = new VBox();
        vbImage.setAlignment(Pos.CENTER_RIGHT);
        vbImage.getChildren().add(imageView);

        vbImage.setPadding(new Insets(0, 0, 0, 20));

        vboxMain = new VBox();
        vboxMain.setAlignment(Pos.CENTER_LEFT);
        vboxMain.setPadding(new Insets(10, 0, 10, 30));

        vboxKiwi = new VBox();

        hbBarcode = new HBox();

        hbBarcode.setPadding(new Insets(5, 5, 5, 5));
        hbBarcode.setAlignment(Pos.CENTER_LEFT);

        lbBarcode = new Label("Brcode: ");
        lbBarcode.setPrefWidth(60);

        lbRackBarcode = new Label("");
        lbRackBarcode.setFont(new Font(25));

        tfRackBarcode = new TextField();

        if (isBCR) {
            hbBarcode.getChildren().addAll(lbBarcode, lbRackBarcode);
        } else {
            hbBarcode.getChildren().addAll(lbBarcode, tfRackBarcode);
        }

        vboxKiwi.getChildren().add(hbBarcode);

        hbLabware = new HBox();
        hbLabware.setPadding(new Insets(5, 5, 5, 5));
        hbLabware.setAlignment(Pos.CENTER_LEFT);

        lbLabwareType = new Label("Type: ");
        lbLabwareType.setPrefWidth(60);

        cbLabware = new ChoiceBox();

        cbLabware.getSelectionModel().selectFirst();

        hbLabware.getChildren().addAll(lbLabwareType, cbLabware);

        vboxKiwi.getChildren().add(hbLabware);

        hbEmpty = new HBox();
        hbEmpty.setPadding(new Insets(10, 5, 5, 5));
        hbEmpty.setAlignment(Pos.CENTER_LEFT);

        cbEmpty = new CheckBox("Empty");

        hbEmpty.getChildren().addAll(cbEmpty);

        vboxKiwi.getChildren().add(hbEmpty);

        vboxMain.getChildren().add(vboxKiwi);

        // Buttons
        Button btnOk = new Button("OK");
        Button btnCancel = new Button("Cancel");

        btnOk.setPrefWidth(90);
        btnOk.setPrefHeight(30);
        btnOk.setMinHeight(30);

        btnCancel.setPrefWidth(90);
        btnCancel.setPrefHeight(30);

        HBox hbBtn = new HBox();

        hbBtn.setAlignment(Pos.CENTER_RIGHT);

        btnOk.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (!isBCR) {
                    Barcode = tfRackBarcode.getText();
                }

                if (Barcode.isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Enter a barcode!");
                    alert.showAndWait();

                }

                System.out.println((String) cbLabware.getSelectionModel().getSelectedItem());

                String TubeType = (String) cbLabware.getSelectionModel().getSelectedItem();

                boolean IsEmpty;

                if (cbEmpty.isSelected()) {
                    IsEmpty = true;
                } else {
                    IsEmpty = false;
                }

                saveRack(Barcode, TubeType, IsEmpty);

                importStage.DrawImportTable();

                close();

            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                close();

            }
        });

        hbBtn.setPrefHeight(60);

        hbBtn.setPadding(new Insets(10, 20, 20, 10));
        hbBtn.setSpacing(20);

        hbBtn.getChildren().addAll(btnOk, btnCancel);

        root.setLeft(vbImage);
        root.setCenter(vboxMain);
        root.setBottom(hbBtn);

        setTitle("Importing of new rack");

        scene = new Scene(root, 400, 220);

        setScene(scene);

        setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {

                importStage.setimportRackStage(null);
            }

        });

    }

    public void setBarcode(String Barcode) {

        lbRackBarcode.setText(Barcode);
        this.Barcode = Barcode;

    }

    public void setLabware(List<PartitionTubeTypes> PartitionTubeTypesList) {

        this.PartitionTubeTypesList = PartitionTubeTypesList;

        cbLabware.getItems().clear();

        for (PartitionTubeTypes ptt : PartitionTubeTypesList) {

            for (RackTubeType rtt : ptt.getTubesTypes()) {

                if (rtt.getTubeTypeID() != 0) {
                    cbLabware.getItems().add(rtt.getTubeTypeName());
                }

            }

        }

        cbLabware.getSelectionModel().selectFirst();

    }

    public void saveRack(String Barcode, String TubeType, boolean isEmpty) {

        int isExists = dm.IsImportRackExists(Barcode);

        if (isExists == 1) {

            JOptionPane.showMessageDialog(null, "Rack with barcode: \"" + Barcode + "\" already exists!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;

        } else if (isExists == 2) {
            JOptionPane.showMessageDialog(null, "Rack with barcode: \"" + Barcode + "\" already imported!", "Error!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create ImportJob
        int RackType = 0;
        int TubeTypeId = 0;
        String TargetPartition = "";

        for (PartitionTubeTypes ptt : PartitionTubeTypesList) {

            for (RackTubeType rtt : ptt.getTubesTypes()) {

                if (rtt.getTubeTypeName().equals(TubeType)) {

                    RackType = rtt.getRackTypeID();
                    TubeTypeId = rtt.getTubeTypeID();
                    TargetPartition = ptt.getPartitionName();

                    break;
                }
            }
        }

        dm.CreateImportJob(Barcode, RackType, TubeTypeId, isEmpty, TargetPartition, user.getUserID());

    }

}
