package com.liconic.stages;

import com.liconic.binding.sys.Cmd;
import com.liconic.binding.sys.ObjectFactory;
import com.liconic.binding.sys.Sys;
import com.liconic.rest.client.ExportClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.Logger;

public class ExportRackStage extends Stage {

    private Scene scene;

    private BorderPane root;
    private HBox hBoxButton;

    private Button btnClose;

    private HBox hBoxInfo;
    private Label lbInfo;

    private HBox hBoxIndicator;
    private ProgressIndicator progressIndicator;
    private ImportStage importStage;

    private int CmdId = 0;
    private int IdRack = 0;

    private Logger log;
    private String WebServiceUri;

    private boolean isClose = false;

    private ObjectFactory of;

    public ExportRackStage(ImportStage importStage, String WebServiceUri, int CmdId, int idRack, Logger log) {

        this.importStage = importStage;
        this.log = log;
        this.WebServiceUri = WebServiceUri;
        this.CmdId = CmdId;
        this.IdRack = idRack;

        of = new ObjectFactory();

        log.info("Open Export Rack window Task=" + CmdId);

        initStyle(StageStyle.UTILITY);

        root = new BorderPane();

        hBoxButton = new HBox();
        hBoxButton.setAlignment(Pos.CENTER);

        btnClose = new Button("Ok");
        btnClose.setPrefWidth(60);
        btnClose.setDisable(true);

        hBoxButton.setPadding(new Insets(10, 0, 10, 0));
        hBoxButton.getChildren().add(btnClose);

        hBoxIndicator = new HBox();

        hBoxIndicator.setAlignment(Pos.CENTER);

        progressIndicator = new ProgressIndicator();

        hBoxIndicator.setPrefSize(100, 100);

        hBoxIndicator.getChildren().add(progressIndicator);

        hBoxInfo = new HBox();

        hBoxInfo.setAlignment(Pos.CENTER_LEFT);

        lbInfo = new Label("Apply Export command...");

        hBoxInfo.getChildren().add(lbInfo);

        root.setCenter(hBoxInfo);

        root.setLeft(hBoxIndicator);

        root.setBottom(hBoxButton);

        setOnShown(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                RunExport();
            }

        });

        scene = new Scene(root, 320, 150);

        setTitle("Export Rack");
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
                isClose = true;
                importStage.setExportRackStage(null);
            }

        });

    }

    public void RunExport() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            ExportClient exportClient = new ExportClient(WebServiceUri, log);

                            Sys sys = exportClient.runExportRack(CmdId, IdRack, importStage.getUser().getLogin());

                            Cmd cmd = sys.getCmds().getValue().getCmd().get(0);

                            if (CmdId == 0) {
                                try {
                                    CmdId = Integer.valueOf(cmd.getId());
                                } catch (Exception E) {
                                }
                            } else {
                                cmd.setId(String.valueOf(CmdId));
                            }

                            if (cmd.getResult().getValue().equals("Error")) {

                                // Change Icon
                                lbInfo.setText(cmd.getValue().getValue());
                                lbInfo.setTextFill(Color.RED);

                                ImageView imageView = new ImageView(new Image("images/warning-icon.png"));

                                hBoxIndicator.getChildren().clear();
                                hBoxIndicator.getChildren().add(imageView);

                                btnClose.setDisable(false);

                            } else {

                                lbInfo.setText("Run Export command...");

                                CmdId = Integer.valueOf(cmd.getId());
                            }

                        } catch (Exception E) {

                            lbInfo.setText("Error: " + E.getMessage());

                            lbInfo.setTextFill(Color.RED);

                            ImageView imageView = new ImageView(new Image("images/warning-icon.png"));

                            hBoxIndicator.getChildren().clear();
                            hBoxIndicator.getChildren().add(imageView);

                            btnClose.setDisable(false);

                        }

                    }

                });
            }
        }).start();

    }

    public void SetCmdReply(int IdCmd, String CmdResult, String CmdValue) {

        if (IdCmd == CmdId) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {

                            ImageView imageView;

                            lbInfo.setText(CmdValue);

                            if (CmdResult.equals("Error")) {
                                lbInfo.setTextFill(Color.RED);
                                imageView = new ImageView(new Image("images/warning-icon.png"));

                            } else {
                                imageView = new ImageView(new Image("images/info-icon.png"));
                                AutoClose();
                            }

                            hBoxIndicator.getChildren().clear();
                            hBoxIndicator.getChildren().add(imageView);

                            btnClose.setDisable(false);
                        }
                    });
                }

            }).start();
        }
    }

    public void AutoClose() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        int count = 0;

                        while (!isClose) {

                            try {
                                Thread.sleep(100);
                                count++;

                                if (count >= 30) {
                                    break;
                                }

                            } catch (Exception E) {

                            }

                        }

                        try {
                            if (!isClose) {
                                close();
                            }
                        } catch (Exception E) {

                        }

                    }
                });
            }

        }).start();

    }

}
