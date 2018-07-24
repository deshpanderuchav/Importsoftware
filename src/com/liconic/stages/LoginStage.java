package com.liconic.stages;

import com.liconic.db.DBTimer;
import com.liconic.user.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.liconic.db.DM;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.apache.logging.log4j.Logger;

public class LoginStage extends Stage {

    private Logger log;

    private Scene scene;
    private BorderPane root;
    private Image image;
    private ImageView imageView;
    private VBox vboxMain;
    private VBox vboxKiwi;

    private TextField tfKIWIName;
    private PasswordField tfKIWIPassword;

    private Button btnOk;
    private Button btnCancel;

    private String DB_DRIVER;
    private String DB_URL;

    private ImportStage importStage;

    public String getDB_DRIVER() {
        return DB_DRIVER;
    }

    public void setDB_DRIVER(String DB_DRIVER) {
        this.DB_DRIVER = DB_DRIVER;
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public void setDB_URL(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    public void setImportStage(ImportStage importStage) {
        this.importStage = importStage;
    }

    public LoginStage(Logger log) {

        this.log = log;

        initStyle(StageStyle.UTILITY);

        root = new BorderPane();

        root.setPadding(new Insets(10, 10, 10, 10));

        image = new Image("images/Liclogo.png");

        imageView = new ImageView();
        imageView.setImage(image);

        VBox vbImage = new VBox();
        vbImage.setAlignment(Pos.CENTER_RIGHT);
        vbImage.getChildren().add(imageView);

        vboxMain = new VBox();
        vboxMain.setAlignment(Pos.CENTER_LEFT);
        vboxMain.setPadding(new Insets(10, 10, 10, 10));

        vboxKiwi = new VBox();

        HBox hbKiwiName = new HBox();

        hbKiwiName.setPadding(new Insets(10, 10, 10, 10));
        hbKiwiName.setAlignment(Pos.CENTER_LEFT);

        Label lbKIWIName = new Label("User Name ");
        lbKIWIName.setPrefWidth(80);

        tfKIWIName = new TextField("");
        tfKIWIName.setDisable(false);

        tfKIWIName.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ENTER) {
                    tfKIWIPassword.requestFocus();
                }
            }

        });

        hbKiwiName.getChildren().addAll(lbKIWIName, tfKIWIName);

        vboxKiwi.getChildren().add(hbKiwiName);

        HBox hbKiwiPassword = new HBox();
        hbKiwiPassword.setPadding(new Insets(10, 10, 10, 10));
        hbKiwiPassword.setAlignment(Pos.CENTER_LEFT);

        Label lbKIWIPassword = new Label("Password ");
        lbKIWIPassword.setPrefWidth(80);
        tfKIWIPassword = new PasswordField();
        tfKIWIPassword.setDisable(false);

        tfKIWIPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ENTER) {

                    DM dm = null;
                    User user = null;

                    try {

                        String LicUserName = tfKIWIName.getText();
                        String LicPassword = "";

                        System.out.println("Login User: " + LicUserName);

                        if (LicUserName.indexOf(".") > 0) {

                            String RegUserName = LicUserName + "@regeneron.com";

                            System.out.println("Login Regeneron");

                            if (!LDAPLogin(RegUserName, tfKIWIPassword.getText())) {
                                System.out.println("Login Regeneron Failure");
                                Platform.exit();
                                close();
                            }

                            System.out.println("Login Regeneron OK");

                            LicPassword = tfKIWIName.getText();

                        } else {
                            LicPassword = tfKIWIPassword.getText();
                        }

                        System.out.println("Login Liconic");

                        System.out.println("Login: User name=" + LicUserName + ". - Password=" + LicPassword + ".");

                        dm = new DM(DB_DRIVER, DB_URL, LicUserName, LicPassword, log);

                        user = dm.getUser(tfKIWIName.getText());

                        if (user == null) {

                            log.error("Login: User is unknown, User name=" + tfKIWIName.getText());

                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("User is unknown!");
                            alert.showAndWait();

                        }

                        importStage.setDm(dm);
                        importStage.setUser(user);

                        log.info("Login: User name=" + tfKIWIName.getText());

                        System.out.println("Login: User name=" + tfKIWIName.getText() + " - Password=" + LicPassword);

                        importStage.setdBTimer(DB_DRIVER, DB_URL, tfKIWIName.getText(), LicPassword, log);

                        importStage.setUserName(tfKIWIName.getText());
                        importStage.setUserPassword(LicPassword);

                    } catch (Exception E) {

                        System.out.println("Login Liconic Failure");

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("ERROR: Create Database connection - " + E.getMessage());
                        alert.showAndWait();

                        log.error("Login: " + E.getMessage());
                        log.info("Login Close Application");
                        Platform.exit();
                    }

                    close();

                }
            }

        });

        hbKiwiPassword.getChildren().addAll(lbKIWIPassword, tfKIWIPassword);

        vboxKiwi.getChildren().add(hbKiwiPassword);

        vboxMain.getChildren().add(vboxKiwi);

        btnOk = new Button("OK");
        btnCancel = new Button("Cancel");

        btnOk.setPrefWidth(90);
        btnOk.setPrefHeight(30);
        btnOk.setMinHeight(30);

        btnCancel.setPrefWidth(90);
        btnCancel.setPrefHeight(30);

        HBox hbBtn = new HBox();
        HBox vbbtnOk = new HBox();
        HBox vbbtnCancel = new HBox();

        hbBtn.setAlignment(Pos.CENTER);
        vbbtnOk.setAlignment(Pos.CENTER);
        vbbtnCancel.setAlignment(Pos.CENTER);

        btnOk.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                DM dm = null;
                User user = null;

                try {

                    String LicUserName = tfKIWIName.getText();
                    String LicPassword = tfKIWIPassword.getText();

                    System.out.println("Login User: " + LicUserName);

                    System.out.println("Login Liconic");

                    System.out.println("Login: User name=" + LicUserName + ". - Password=" + LicPassword + ".");

                    dm = new DM(DB_DRIVER, DB_URL, LicUserName, LicPassword, log);

                    user = dm.getUser(tfKIWIName.getText());

                    if (user == null) {

                        log.error("Login: User is unknown, User name=" + tfKIWIName.getText());

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("User is unknown!");
                        alert.showAndWait();

                    } else {
                        System.out.println("userId=" + user.getUserID());
                    }

                    importStage.setDm(dm);
                    importStage.setUser(user);

                    log.info("Login: User name=" + tfKIWIName.getText());

                    System.out.println("Login: User name=" + tfKIWIName.getText() + " - Password=" + LicPassword);

                    importStage.setdBTimer(DB_DRIVER, DB_URL, tfKIWIName.getText(), LicPassword, log);

                    importStage.setUserName(tfKIWIName.getText());
                    importStage.setUserPassword(LicPassword);

                } catch (Exception E) {

                    System.out.println("Login Liconic Failure");

                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("ERROR: Create Database connection - " + E.getMessage());
                    alert.showAndWait();

                    log.error("Login: " + E.getMessage());
                    log.info("Login Close Application");
                    Platform.exit();
                }

                close();
            }
        });

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                log.info("Login Close Application");
                System.out.println("Close");
                close();
                Platform.exit();

            }
        });

        hbBtn.setPrefHeight(60);

        hbBtn.setPadding(new Insets(10, 10, 10, 10));
        vbbtnOk.setPadding(new Insets(10, 30, 10, 10));
        vbbtnCancel.setPadding(new Insets(10, 10, 10, 30));

        vbbtnOk.getChildren().add(btnOk);
        vbbtnCancel.getChildren().add(btnCancel);

        hbBtn.getChildren().addAll(vbbtnOk, btnCancel);

        root.setLeft(vbImage);
        root.setCenter(vboxMain);
        root.setBottom(hbBtn);

        scene = new Scene(root, 400, 240);

        setScene(scene);
    }

    public boolean LDAPLogin(String name, String password) {

        Hashtable<String, Object> env = new Hashtable<String, Object>(11);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://regeneron.regn.com");

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, name);
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            // Create initial context
            System.out.println("1");
            DirContext ctx = new InitialDirContext(env);

            System.out.println("2");
            System.out.println(ctx.lookup("OU=Regeneron Users,DC=regeneron,DC=regn,DC=com"));

            System.out.println("Class NAME=" + ctx.lookup("OU=Regeneron Users,DC=regeneron,DC=regn,DC=com").getClass().getName());

            System.out.println("3");

            System.out.println("Name space:" + ctx.getNameInNamespace());

            System.out.println("Attribures:" + ctx.getAttributes("OU=Regeneron Users,DC=regeneron,DC=regn,DC=com").toString());

            ctx.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Authorization Failed!");

            // Create expandable Exception.
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            // Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();

            return false;

        }

    }

}
