package com.liconic.stages;

import com.liconic.binding.sys.ObjectFactory;
import com.liconic.binding.stx.STXRequest;
import com.liconic.rest.client.ExportJobClient;

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

public class ExportJobStage extends Stage {

    private Scene scene;

    private BorderPane root;
    private HBox hBoxButton;

    private Button btnClose;

    private HBox hBoxInfo;
    private Label lbInfo;

    private HBox hBoxIndicator;
    private ProgressIndicator progressIndicator;
    private ImportStage importStage;

    private int cmdId = 0;
    private String jobId;
    private String plateBCR;

    private Logger log;
    private String webServiceUri;

    private boolean isClose = false;

    private ObjectFactory of;

    public ExportJobStage(ImportStage importStage, String webServiceUri, int cmdId, String jobId, String plateBCR, Logger log) {

        this.importStage = importStage;
        this.log = log;
        this.webServiceUri = webServiceUri;
        this.cmdId = cmdId;
        this.jobId = jobId;
        this.plateBCR = plateBCR;

        of = new ObjectFactory();

        log.info("Open Export Job window Task=" + cmdId);

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

        setTitle("Export Job");
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
                importStage.setExportJobStage(null);
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
                            ExportJobClient exportClient = new ExportJobClient(webServiceUri, log);

                            STXRequest response = exportClient.runExportJob(jobId, plateBCR, importStage.getUser().getLogin());

                            String status = "";
                            String errorMsg = "";
                            if ((null != response) && (null != response.getAnswer())) {
                                status = response.getAnswer().getStatus();
                                errorMsg = response.getAnswer().getErrMsg();
                            }

                            if ("ERR".equals(status)) {
                                // Change Icon
                                lbInfo.setText(errorMsg);
                                lbInfo.setTextFill(Color.RED);

                                ImageView imageView = new ImageView(new Image("images/warning-icon.png"));

                                hBoxIndicator.getChildren().clear();
                                hBoxIndicator.getChildren().add(imageView);

                                btnClose.setDisable(false);

                            } else {
                                lbInfo.setText("Export job ran successfully!");
                                hBoxIndicator.getChildren().clear();
                                btnClose.setDisable(false);
                            }

                        } catch (Exception e) {
                            lbInfo.setText("Error: " + e.getMessage());
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

    public void SetCmdReply(int idCmd, String cmdResult, String cmdValue) {

        if (idCmd == cmdId) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            ImageView imageView;

                            lbInfo.setText(cmdValue);

                            if (cmdResult.equals("Error")) {
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

                            } catch (Exception e) {
                            }
                        }

                        try {
                            if (!isClose) {
                                close();
                            }
                        } catch (Exception e) {

                        }
                    }
                });
            }

        }).start();
    }

}
