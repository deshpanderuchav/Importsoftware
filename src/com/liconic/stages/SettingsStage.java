package com.liconic.stages;

import com.liconic.table.settings.SettingsTableRecord;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.Logger;

public class SettingsStage extends Stage {

    private Logger log;

    private Scene scene;

    private BorderPane root;

    private HBox hboxButton;
    private TableView tableSettings;
    private TableColumn columnParamName;
    private TableColumn columnParamValue;

    private Button btnClose;

    private ImportStage importStage;

    public SettingsStage(ImportStage importStage, Logger log) {

        this.importStage = importStage;
        this.log = log;

        initStyle(StageStyle.UTILITY);

        root = new BorderPane();

        root.setPadding(new Insets(10, 10, 10, 10));

        hboxButton = new HBox();
        hboxButton.setAlignment(Pos.CENTER);
        hboxButton.setPadding(new Insets(10, 10, 10, 10));
        hboxButton.setPrefHeight(60);

        btnClose = new Button("Close");

        btnClose.setPrefWidth(90);
        btnClose.setPrefHeight(30);
        btnClose.setMinHeight(30);

        btnClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                close();

            }
        });

        tableSettings = new TableView();

        columnParamName = new TableColumn("Parameter");

        tableSettings.setEditable(true);

        columnParamName.setMinWidth(170);
        columnParamName.setCellValueFactory(new PropertyValueFactory<SettingsTableRecord, String>("paramName"));

        columnParamValue = new TableColumn("Value");
        columnParamValue.setMinWidth(70);
        columnParamValue.setCellValueFactory(new PropertyValueFactory<SettingsTableRecord, String>("paramValue"));

        columnParamValue.setCellFactory(TextFieldTableCell.forTableColumn());
        columnParamValue.setOnEditCommit(
                new EventHandler<CellEditEvent<SettingsTableRecord, String>>() {
            @Override
            public void handle(CellEditEvent<SettingsTableRecord, String> t) {

                try {

                    int id = ((SettingsTableRecord) t.getTableView().getItems().get(t.getTablePosition().getRow())).getId();
                    int value = Integer.valueOf(t.getNewValue());

                    importStage.getDm().UpdateSettings(id, value);

                    ((SettingsTableRecord) t.getTableView().getItems().get(t.getTablePosition().getRow())).setParamValue(t.getNewValue());

                } catch (Exception E) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(E.getMessage());
                    alert.showAndWait();
                }

            }
        }
        );

        tableSettings.getColumns().addAll(columnParamName, columnParamValue);

        tableSettings.setItems(importStage.getDm().getSettings());

        hboxButton.getChildren().addAll(btnClose);

        root.setCenter(tableSettings);
        root.setBottom(hboxButton);

        scene = new Scene(root, 300, 300);

        setScene(scene);
    }

}
