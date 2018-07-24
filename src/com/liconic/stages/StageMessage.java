package com.liconic.stages;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class StageMessage extends Stage {

    private boolean isError = false;

    private Scene scene;

    private BorderPane root;
    private HBox hBoxButton;

    private Button btnClose;

    private HBox hBoxInfo;
    private Label lbInfo;

    private HBox hBoxIndicator;

    ImageView imageView;

    public StageMessage(int typeMessage, String titleMessage, String infoMessage) {

        initStyle(StageStyle.UTILITY);

        if (typeMessage == 1) {

            imageView = new ImageView(new Image("images/info-icon.png"));

        } else if (typeMessage == 2) {

            imageView = new ImageView(new Image("images/warning-icon.png"));

        } else if (typeMessage == 3) {

            imageView = new ImageView(new Image("images/error-icon.png"));

        }

        root = new BorderPane();

        hBoxButton = new HBox();
        hBoxButton.setAlignment(Pos.CENTER);

        btnClose = new Button("Ok");
        btnClose.setPrefWidth(60);

        hBoxButton.setPadding(new Insets(10, 0, 10, 0));
        hBoxButton.getChildren().add(btnClose);

        hBoxIndicator = new HBox();

        hBoxIndicator.setAlignment(Pos.CENTER);

        hBoxIndicator.setPrefSize(100, 100);

        hBoxIndicator.getChildren().add(imageView);

        hBoxInfo = new HBox();

        hBoxInfo.setAlignment(Pos.CENTER_LEFT);

        lbInfo = new Label(infoMessage);
        lbInfo.setWrapText(true);

        hBoxInfo.getChildren().add(lbInfo);

        root.setCenter(hBoxInfo);

        root.setLeft(hBoxIndicator);

        root.setBottom(hBoxButton);

        setOnShown(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                //RunXferImport();
            }

        });

        scene = new Scene(root, 320, 150);

        setTitle(titleMessage);
        setScene(scene);

        btnClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                close();

            }
        });

        setOnHiding(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {

//                importStage.setImportForm(null);
            }

        });

    }

}
