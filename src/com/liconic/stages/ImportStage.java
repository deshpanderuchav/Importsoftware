package com.liconic.stages;

import com.liconic.binding.conffiles.Parameter;
import com.liconic.binding.conffiles.ParameterGroup;
import com.liconic.binding.sys.Cmd;
import com.liconic.binding.sys.Cmds;
import com.liconic.binding.sys.Scheduler;
import com.liconic.binding.sys.Sys;
import com.liconic.buffergui.BufferModel;
import com.liconic.db.DBEventNotifier;
import com.liconic.db.DBTimer;
import com.liconic.db.DM;
import com.liconic.hardware.Cassette;
import com.liconic.hardware.Device;
import com.liconic.hardware.KIWISystem;
import com.liconic.hardware.Level;
import com.liconic.labware.Plate;
import com.liconic.labware.configuration.PartitionTubeTypes;
import com.liconic.labware.configuration.RackTubeType;
import com.liconic.rest.client.ExportClient;
import com.liconic.rest.client.Fnclient;
import com.liconic.rest.client.SchedulerClient;
import com.liconic.serial.DataLogicBCReader;
import com.liconic.stages.gui.DeviceStatusTab;
import com.liconic.table.exporttasks.ExportExceptioRacksModel;
import com.liconic.table.exporttasks.ExportTubePickModel;
import com.liconic.table.exporttasks.ExportPlateModel;
import com.liconic.table.exporttasks.ExportTaskCellFactory;
import com.liconic.table.exporttasks.ExportTaskTableModel;
import com.liconic.table.importtasks.ImportTaskCellFactory;
import com.liconic.table.importtasks.ImportTaskTableModel;
import com.liconic.table.importtasks.TaskHistoryModel;
import com.liconic.table.importtasks.TaskPropertyModel;
import com.liconic.timers.LogOutTimer;
import com.liconic.user.User;
import com.liconic.timers.WarmUpTimer;
import com.liconic.wsclient.WebsocketClientEndpoint;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;	  
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import static jdk.nashorn.internal.objects.NativeRegExp.source;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class ImportStage extends Application {

    static Logger log = LogManager.getLogger(ImportStage.class.getName());
    private WebsocketClientEndpoint clientEndPoint = null;

    private BorderPane rootPane;

    private HBox statusBar;

    private DM dm = null;
    private User user = null;

    private String DB_DRIVER;
    private String DB_URL;
    private String DB_HOST;
    private String DB_PATH;
    private String WEB_SOCKET_URI;
    private String WEB_SERVICES_URI;

    private String ConfigFile;

    private KIWISystem Sys;

    private int IdStoreDevice = 0;
    private int IdBufferDevice = 0;

    private HBox hbSchedulerStatus;
    private HBox hbEmptyLevels;
    private HBox hbEmptyRacks;
    private HBox hbWarmUpTime;

    private Label lbSchedulerStatus;
    private Label lbEmptyLevels;
    private Label lbEmptyRacks;
    private Label lbWarmUpTime;

    private String UserName;
    private String UserPassword;

    // Toolbar
    private BorderPane toolBarContentPane;
    private ToolBar toolBarPane;
    private ToolBar logoBox;
    private ImageView logo;

    private MenuBar menuBar;
    private Menu menuFile;
    private Menu menuConnect;
    private Menu menuTools;

    // ToolBar Buttons
    private Button btnDoor;
    private Button btnXfer;

    private SplitPane splitPane;

    //Left Panel
    private SplitPane leftSplitpane;
    private TitledPane titledPaneBuffer;
    private TitledPane titledPaneExceptionRacks;

    private TreeTableView bufferTableView;
    private TreeTableColumn<BufferModel, Object> positionColumn;
    private TreeTableColumn<BufferModel, String> noteColumn;
    private TableView exceptionTable;

    // Import
    private SplitPane SplitPaneImport;

    private TreeTableView importTreeTableView;

    private TreeTableColumn importColumnBarcode;
    private TreeTableColumn importColumnTask;
    private TreeTableColumn importColumnStatus;
    private TreeTableColumn importColumnNote;

    private MenuItem menuDeleteJob;

    private TitledPane titledPaneImportProperties;
    private TitledPane titledPaneImportHistory;

    private TableView importPropertiesTableView;
    private TableView importHistoryTableView;

    private ObservableList<TaskHistoryModel> historyTaskTableData = FXCollections.observableArrayList();
    private ObservableList<TaskPropertyModel> propertyTaskTableData = FXCollections.observableArrayList();

    private ObservableList<TaskHistoryModel> historyExportTaskTableData = FXCollections.observableArrayList();

    private Image IconCass = null;
    private Image IconLvl = null;
    private Image IconPlate = null;

    private List<PartitionTubeTypes> PartitionTubeTypesList;
    private DataLogicBCReader BCReader = null;

    private ImportRackStage importRackStage;

    private Scene scene;
    private ImportStage importStage;

    private String ManualScannerPort = "";

    private ImportForm importForm;
    private ImportDoor importDoor;

    private ExportRackStage exportRackStage;

    private TabPane tabPane;
    private Tab tabImport;
    private Tab tabExport;

    // Central pane
    private BorderPane borderPaneCenter;

    private DBTimer dBTimer = null;
    private WarmUpTimer warmUpTimer = null;

    private DBEventNotifier en = null;

    private boolean IsLogout = false;

    private Date lastAccessDate = null;
    private LogOutTimer logOutTimer = null;

    private double dividerTask = 0.5;
    private double dividerHistory = 0.75;

    private TreeItem<ImportTaskTableModel> ImportRootItem = null;

    // Export
    private SplitPane SplitPaneExport;

    private TreeTableView exportTreeTableView;

    private TreeItem<ExportTaskTableModel> ExportRootItem = null;

    private TitledPane titledPaneExportDetails;
    private TableView exportContentTable;
    private TableView exportRacksTable;

    ObservableList<ExportPlateModel> propertyExportTask = FXCollections.observableArrayList();

    private TitledPane titledPaneExportHistory;
    private TableView exportHistoryTable;

    private List<Integer> bufferOpennedNodes = new ArrayList<>();

    public ImportStage() {
        PartitionTubeTypesList = new ArrayList<>();
        importStage = this;
    }

    @Override
    public void init() throws Exception {

        super.init();

        Format formatter = new SimpleDateFormat("dd.MM.yyyy");
        log.info("RUN: " + formatter.format(new Date()));

        final Parameters params = getParameters();
        final List<String> parameters = params.getRaw();

        for (int i = 0; i < parameters.size(); i++) {

            if (!parameters.get(i).isEmpty()) {

            }
        }

        Map param = getParameters().getNamed();

        ConfigFile = (String) param.get("ConfigFile");

        if (ConfigFile == null) {

            File file = new File(System.getProperty("user.dir") + "\\ImportConfig.xml");

            if (file.exists()) {
                ConfigFile = System.getProperty("user.dir") + "\\ImportConfig.xml";
            } else {
                log.error("File does not exists 1: " + ConfigFile);
            }

        } else {

            File file = new File(ConfigFile);

            if (!file.exists()) {
                log.error("File does not exists 2: " + ConfigFile);
                ConfigFile = "";
            }

        }

        if ((ConfigFile == null) || (ConfigFile.isEmpty())) {
            ConfigFile = "D:\\ImportConfig.xml";
        }

        File file = new File(ConfigFile);

        if (!file.exists()) {

            log.error("File does not exists 3: " + ConfigFile);

            ConfigFile = "";

            JOptionPane.showMessageDialog(null, "Configuration File: \"ImportConfig.xml\" has not been found!", "Error!", JOptionPane.ERROR_MESSAGE);

            log.error("Exit, configuration file has not been found");

            Platform.exit();

        } else {

            ReadConfigFile(ConfigFile);
        }

    }

    @Override
    public void start(Stage primaryStage) {

// Create Icons
        IconCass = new Image("images/cass.png");
        IconLvl = new Image("images/level.png");
        IconPlate = new Image("images/plate.png");

        rootPane = new BorderPane();

        statusBar = new HBox();
        statusBar.setSpacing(10);
        statusBar.setPadding(new Insets(0, 5, 0, 5));

        statusBar.setAlignment(Pos.CENTER_LEFT);

        toolBarContentPane = new BorderPane();

        ImageView doorImg = new ImageView(new Image("images/doorclose_btn.png"));
        doorImg.setFitHeight(38);
        doorImg.setFitWidth(33);

        btnDoor = new Button("", doorImg);
        btnDoor.setTooltip(new Tooltip("Import Samples via Door"));
        btnDoor.setPrefSize(40, 40);

        btnDoor.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (!dm.getWarupDelay()) {

                    importDoor = new ImportDoor(importStage, WEB_SERVICES_URI, log);
                    importDoor.initModality(Modality.WINDOW_MODAL);
                    importDoor.initOwner(scene.getWindow());

                    importDoor.show();

                } else {

                    StageMessage stageMessage = new StageMessage(3, "Warning", "There is pending task in buffer!\n\nPlease, try again later!");
                    stageMessage.initModality(Modality.WINDOW_MODAL);
                    stageMessage.initOwner(scene.getWindow());

                    stageMessage.show();
                }

            }
        });

        ImageView xferImg = new ImageView(new Image("images/xfer_btn.png"));
        xferImg.setFitHeight(38);
        xferImg.setFitWidth(33);

        btnXfer = new Button("", xferImg);
        btnXfer.setTooltip(new Tooltip("Import Samples via Transferstation"));
        btnXfer.setPrefSize(40, 40);

        btnXfer.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                importForm = new ImportForm(importStage, WEB_SERVICES_URI, log);
                importForm.initModality(Modality.WINDOW_MODAL);
                importForm.initOwner(scene.getWindow());
                importForm.show();

            }
        });

        toolBarPane = new ToolBar(btnDoor, btnXfer);
        toolBarPane.setPrefHeight(40);

        toolBarContentPane.setCenter(toolBarPane);
        logo = new ImageView(new Image("images/liconic-logo.png"));
        logo.setFitHeight(40);
        logo.setFitWidth(175);

        logoBox = new ToolBar(logo);
        logoBox.setPrefHeight(40);
        toolBarContentPane.setRight(logoBox);

        menuBar = new MenuBar();

        menuFile = new Menu("File");

        MenuItem miExit = new MenuItem("Exit");

        menuFile.getItems().addAll(/*miLogOut, new SeparatorMenuItem(), */miExit);

        menuConnect = new Menu("Connect");
        MenuItem miConnect = new MenuItem("Connect to scheduler");
        MenuItem miUpdateContent = new MenuItem("Update content");
        menuConnect.getItems().addAll(miConnect, new SeparatorMenuItem(), miUpdateContent);
        /*        
        miLogOut.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {                                
                LogOut();
            }
        });
         */

        miUpdateContent.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                UpdateContentGUI();

            }
        });

        miConnect.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                if (clientEndPoint == null) {
                    ConnectToWebsocket();
                }
            }
        });

        menuTools = new Menu("Tools");
        MenuItem miSettings = new MenuItem("Settings");

        miSettings.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                SettingsStage settingsForm = new SettingsStage(importStage, log);
                settingsForm.initModality(Modality.WINDOW_MODAL);
                settingsForm.initOwner(scene.getWindow());
                settingsForm.show();

            }
        });

        MenuItem miImportPlate = new MenuItem("Import Plate");

        miImportPlate.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                importRackStage = new ImportRackStage(importStage, user, log, false);

                importRackStage.initModality(Modality.WINDOW_MODAL);

                importRackStage.setLabware(PartitionTubeTypesList);

                importRackStage.initOwner(scene.getWindow());

                importRackStage.show();

            }
        });

        menuTools.getItems().addAll(miSettings, new SeparatorMenuItem(), miImportPlate);

        menuBar.getMenus().addAll(menuFile, menuTools, menuConnect);

        toolBarContentPane.setTop(menuBar);

        rootPane.setTop(toolBarContentPane);

        rootPane.setBottom(statusBar);

        splitPane = new SplitPane();

        leftSplitpane = new SplitPane();

        // Columns
        positionColumn = new TreeTableColumn<>("Position");
        positionColumn.setEditable(false);
        positionColumn.setPrefWidth(130);
        positionColumn.setCellValueFactory(new TreeItemPropertyValueFactory("position"));

        positionColumn.setCellFactory(new Callback<TreeTableColumn<BufferModel, Object>, TreeTableCell<BufferModel, Object>>() {

            @Override
            public TreeTableCell<BufferModel, Object> call(TreeTableColumn<BufferModel, Object> param) {

                TreeTableCell<BufferModel, Object> cell = new TreeTableCell<BufferModel, Object>() {

                    ImageView imageview = new ImageView();

                    @Override

                    public void updateItem(Object object, boolean empty) {

                        if (object != null) {

                            HBox box = new HBox();

                            box.setSpacing(10);

                            VBox vbox = new VBox();

                            String caption = "";

                            if (object.getClass().getName().equals("com.liconic.hardware.Cassette")) {

                                caption = "Cassette " + String.valueOf(((Cassette) object).getID());

                                imageview.setImage(IconCass);

                            } else {
                                if (object.getClass().getName().equals("com.liconic.hardware.Level")) {

                                    Level level = ((Level) object);

                                    if (level.getPlate() == null) {

                                        caption = "Level " + String.valueOf(level.getLevelId());
                                        imageview.setImage(IconLvl);

                                    } else {

                                        if (!level.getPlate().getBarcode().isEmpty()) {
                                            caption = level.getPlate().getBarcode();
                                        } else {
                                            caption = "Plate";
                                        }

                                        imageview.setImage(IconPlate);

                                    }

                                }
                            }

                            vbox.getChildren().add(new Label(caption));

                            box.getChildren().addAll(imageview, vbox);

                            setGraphic(box);

                        } else {
                            HBox box = new HBox();
                            setGraphic(box);
                        }
                    }
                };
                return cell;
            }
        });

        noteColumn = new TreeTableColumn<>("Note");
        noteColumn.setEditable(false);
        noteColumn.setMinWidth(80);
        noteColumn.setCellValueFactory(new TreeItemPropertyValueFactory("note"));

        scene = new Scene(rootPane, Screen.getPrimary().getVisualBounds().getWidth(), 600);

        try {

            String UName = System.getProperty("user.name");
            String UPassword = UName.toLowerCase();

//            UName = "ADMIN";
//            UPassword = "test";            
            dm = new DM(DB_DRIVER, DB_URL, UName, UPassword, log);

            UserName = UName;
            UserPassword = UPassword;

            user = dm.getUser(UName);

            if (user == null) {

                log.error("Login: User is unknown, User name=" + UName);

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User is unknown!");
                alert.showAndWait();

            } else {

                importStage.setDm(dm);
                importStage.setUser(user);

                log.info("Login: User name=" + UName);

            }

        } catch (Exception E) {

            System.out.println("Login Liconic Failure");

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("ERROR: Create Database connection - " + E.getMessage());
            alert.showAndWait();

            //log.error("Login: " + E.getMessage());
            //log.info("Login Close Application");
            //Platform.exit();
        }

        if (user == null) {

            LoginStage ls = new LoginStage(log);

            ls.setDB_DRIVER(DB_DRIVER);
            ls.setDB_URL(DB_URL);
            ls.setImportStage(this);

            ls.showAndWait();
        }

        if (user == null) {

            Platform.exit();

        } else {

            try {

                setdBTimer(DB_DRIVER, DB_URL, UserName, UserPassword, log);

            } catch (Exception E) {
            }

            Sys = dm.getSystem();

            if (Sys == null) {
                JOptionPane.showMessageDialog(null, "Can not create a System from Database!", "Error", JOptionPane.ERROR_MESSAGE);
                Platform.exit();
            }

            if (user.getUserRole() == 1) {

            }

            for (Device dev : Sys.getDevices()) {

                DeviceStatusTab statusTab = new DeviceStatusTab(dev);
                statusBar.getChildren().add(statusTab);

                if (dev.isSTC()) {
                    IdStoreDevice = dev.getId();
                } else {
                    if (dev.isSTB()) {
                        IdBufferDevice = dev.getId();
                    }
                }

            }

            if (!ManualScannerPort.isEmpty()) {

                try {
                    BCReader = new DataLogicBCReader(ManualScannerPort, this, log);
                } catch (Exception E) {

                    JOptionPane.showMessageDialog(null,
                            "Error of running manual BC Reader, Port: " + ManualScannerPort + "!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);

                    BCReader = null;

                }

            }

            bufferTableView = new TreeTableView(dm.getBufferView(IdBufferDevice));

            bufferTableView.setShowRoot(false);
            bufferTableView.setEditable(false);
            bufferTableView.getColumns().setAll(positionColumn, noteColumn);
            bufferTableView.setPlaceholder(new Text(""));

            titledPaneBuffer = new TitledPane();
            titledPaneBuffer.setText("Buffer");

            titledPaneBuffer.setContent(bufferTableView);

            titledPaneExceptionRacks = new TitledPane();
            titledPaneExceptionRacks.setText("Exception Racks");

            exceptionTable = CreateExceptionTable();
            titledPaneExceptionRacks.setContent(exceptionTable);

            DrawExceptionPlates();

            leftSplitpane.setOrientation(Orientation.VERTICAL);

            leftSplitpane.getItems().addAll(titledPaneBuffer, titledPaneExceptionRacks);

            leftSplitpane.setDividerPosition(0, 0.7);

            splitPane.getItems().add(leftSplitpane);

            splitPane.setDividerPosition(0, 0.3);

            tabPane = new TabPane();

            tabImport = new Tab(" Import ");
            tabImport.setClosable(false);

            tabExport = new Tab(" Export ");
            tabExport.setClosable(false);

            tabPane.getTabs().addAll(tabImport, tabExport);

            borderPaneCenter = new BorderPane();

            // ImportTable
            ImportRootItem = dm.getImportTaskList(user.getUserID());
            importTreeTableView = new TreeTableView(ImportRootItem);
            importTreeTableView.setShowRoot(false);
            importTreeTableView.setEditable(false);
            importTreeTableView.setPlaceholder(new Text(""));
            importTreeTableView.getStylesheets().add("style/stylesTaskTree.css");

            importTreeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {

                @Override
                public void changed(ObservableValue<? extends TreeItem> paramObservableValue, TreeItem paramT1, TreeItem selectedItem) {

                    if (selectedItem != null) {

                        if (((ImportTaskTableModel) selectedItem.getValue()).getPlate() != null) {
                            DrawImportJob(((ImportTaskTableModel) selectedItem.getValue()).getId(), ((Plate) ((ImportTaskTableModel) selectedItem.getValue()).getPlate()).getID());
                        } else {
                            DrawImportTask(((ImportTaskTableModel) selectedItem.getValue()).getId());
                        }

                    }

                }

            });

            menuDeleteJob = new MenuItem("Delete Import");

            menuDeleteJob.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {

                    int idJob = 0;

                    if (((ImportTaskTableModel) ((TreeItem) importTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getPlate() != null) {
                        idJob = ((ImportTaskTableModel) ((TreeItem) importTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getId();
                    } else {
                        idJob = ((ImportTaskTableModel) ((TreeItem) importTreeTableView.getSelectionModel().getSelectedItem()).getParent().getValue()).getId();
                    }

                    if (!dm.DeleteImportJob(idJob)) {

                        log.info("Can not delete Import Job ID=" + idJob);

                        StageMessage stageMessage = new StageMessage(3, "Warning", "Can not delete Import Job!\n\nSelected Job has active task!");
                        stageMessage.initModality(Modality.WINDOW_MODAL);
                        stageMessage.initOwner(scene.getWindow());

                        stageMessage.show();

                    } else {

                        log.info("Import Job deleted ID=" + idJob);

                        ImportRootItem = dm.getImportTaskList(user.getUserID());
                        importTreeTableView.setRoot(ImportRootItem);
                    }

                }
            });

            importTreeTableView.setContextMenu(new ContextMenu(menuDeleteJob));

            importColumnBarcode = new TreeTableColumn<>("Barcode");
            importColumnBarcode.setEditable(false);
            importColumnBarcode.setPrefWidth(130);
            importColumnBarcode.setCellValueFactory(new TreeItemPropertyValueFactory("plate"));

            importColumnBarcode.setCellFactory(new Callback<TreeTableColumn<ImportTaskTableModel, Object>, TreeTableCell<ImportTaskTableModel, Object>>() {

                @Override
                public TreeTableCell<ImportTaskTableModel, Object> call(TreeTableColumn<ImportTaskTableModel, Object> param) {

                    TreeTableCell<ImportTaskTableModel, Object> cell = new TreeTableCell<ImportTaskTableModel, Object>() {

                        @Override
                        public void updateItem(Object object, boolean empty) {

                            if (object != null) {

                                HBox box = new HBox();

                                box.setSpacing(10);

                                VBox vbox = new VBox();

                                String caption = "";

                                caption = ((Plate) object).getBarcode();

                                vbox.getChildren().add(new Label(caption));
                                box.getChildren().addAll(vbox);

                                setGraphic(box);

                            } else {
                                HBox box = new HBox();
                                setGraphic(box);
                            }

                        }

                    };

                    return cell;

                }

            });

            importColumnTask = new TreeTableColumn<>("Task");
            importColumnTask.setEditable(false);
            importColumnTask.setMinWidth(120);
            importColumnTask.setCellValueFactory(new TreeItemPropertyValueFactory("task"));
            importColumnTask.setCellFactory(new ImportTaskCellFactory());

            importColumnStatus = new TreeTableColumn<>("Status");
            importColumnStatus.setEditable(false);
            importColumnStatus.setMinWidth(100);
            importColumnStatus.setCellValueFactory(new TreeItemPropertyValueFactory("status"));
            importColumnStatus.setCellFactory(new ImportTaskCellFactory());

            importColumnNote = new TreeTableColumn<>("Note");
            importColumnNote.setEditable(false);
            importColumnNote.setMinWidth(160);
            importColumnNote.setCellValueFactory(new TreeItemPropertyValueFactory("note"));
            importColumnNote.setCellFactory(new ImportTaskCellFactory());

            importTreeTableView.getColumns().setAll(importColumnBarcode, importColumnTask, importColumnStatus, importColumnNote);

            borderPaneCenter.setCenter(tabPane);

            SplitPaneImport = new SplitPane();
            SplitPaneImport.setOrientation(Orientation.VERTICAL);

            titledPaneImportProperties = new TitledPane();
            titledPaneImportProperties.setText("Properties");

            titledPaneImportHistory = new TitledPane();
            titledPaneImportHistory.setText("History");

            importHistoryTableView = CreateimportHistoryTableView();
            importPropertiesTableView = CreateimportPropertiesTableView();

            titledPaneImportProperties.setContent(importPropertiesTableView);
            titledPaneImportHistory.setContent(importHistoryTableView);

            SplitPaneImport.getItems().addAll(importTreeTableView, titledPaneImportProperties, titledPaneImportHistory);

            SplitPaneImport.setDividerPositions(0.5f, 0.75f);

            tabImport.setContent(SplitPaneImport);

            DrawImportTable();

            // Export
            SplitPaneExport = new SplitPane();
            SplitPaneExport.setOrientation(Orientation.VERTICAL);

            titledPaneExportDetails = new TitledPane();
            titledPaneExportDetails.setText("Details");

            titledPaneExportHistory = new TitledPane();
            titledPaneExportHistory.setText("History");

            exportContentTable = CreateExportContentTable();
            exportRacksTable = CreateExportRacksTable();
            exportHistoryTable = CreateExportHistoryTableView();

            titledPaneExportDetails.setContent(exportContentTable);
            titledPaneExportHistory.setContent(exportHistoryTable);

            exportTreeTableView = CreateExportTable();

            SplitPaneExport.getItems().addAll(exportTreeTableView, titledPaneExportDetails, titledPaneExportHistory);

            SplitPaneExport.setDividerPositions(0.5f, 0.75f);

            tabExport.setContent(SplitPaneExport);

            splitPane.getItems().add(borderPaneCenter);

            rootPane.setCenter(splitPane);

            dm.getUserLabware(PartitionTubeTypesList, user.getUserID());

            // Scheduler Status
            hbSchedulerStatus = new HBox();
            hbSchedulerStatus.setMinWidth(140);
            lbSchedulerStatus = new Label("Scheduler: ");

            hbSchedulerStatus.getChildren().add(lbSchedulerStatus);

            statusBar.getChildren().add(hbSchedulerStatus);

            // Empty Levels
            hbEmptyLevels = new HBox();
            hbEmptyLevels.setMinWidth(120);
            lbEmptyLevels = new Label("Empty levels: ");

            hbEmptyLevels.getChildren().add(lbEmptyLevels);

            statusBar.getChildren().add(hbEmptyLevels);

            // Empty Racks
            hbEmptyRacks = new HBox();
            hbEmptyRacks.setMinWidth(120);
            lbEmptyRacks = new Label("Empty racks: ");

            hbEmptyRacks.getChildren().add(lbEmptyRacks);

            statusBar.getChildren().add(hbEmptyRacks);

            // Warmup time 
            hbWarmUpTime = new HBox();
            hbWarmUpTime.setMinWidth(120);
            lbWarmUpTime = new Label("");

            hbWarmUpTime.getChildren().add(lbWarmUpTime);

            statusBar.getChildren().add(hbWarmUpTime);

            DrawCountEmptyLevels();
            DrawCountEmptyRacks();

            en = new DBEventNotifier(DB_HOST, DB_PATH, UserName, UserPassword, this, log);

            en.run();

            primaryStage.setOnShown(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent t) {
                    ConnectToWebsocket();
                }

            });

        }

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {

                IsLogout = true;

                try {

                    if (clientEndPoint != null) {
                        clientEndPoint.Close();
                    }
                } catch (Exception E) {

                }

                if (BCReader != null) {
                    BCReader.ClosePort();
                }

                if (en != null) {
                    en.stop();
                }

                if (clientEndPoint != null) {
                    clientEndPoint.Close();
                }

                if (warmUpTimer != null) {

                    warmUpTimer.Stop();
                }

                if (logOutTimer != null) {
                    logOutTimer.Stop();
                }

            }
        });

        primaryStage.addEventFilter(EventType.ROOT, new EventHandler<Event>() {

            @Override
            public void handle(Event event) {

                if (IsLogout) {
                    return;
                }

                lastAccessDate = new Date();
            }
        }
        );

        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/appliclogo.png")));

        primaryStage.setTitle("Regeneron Import/Export Console [" + user.getUserName() + " " + user.getUserSecName() + "]");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Started");
        log.info("Started");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public DM getDm() {
        return dm;
    }

    public void setDm(DM dm) {
        this.dm = dm;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void ReadConfigFile(String PathToConf) {
        try {

            log.info("Read Config file: " + PathToConf);

            JAXBContext jc = JAXBContext.newInstance(com.liconic.binding.conffiles.ObjectFactory.class);
            Unmarshaller u = jc.createUnmarshaller();

            //File f = new File("C:\\TestDriverContent.xml");
            File f = new File(PathToConf);

            com.liconic.binding.conffiles.Parameters params = (com.liconic.binding.conffiles.Parameters) u.unmarshal(f);

            for (int sys_i = 0; sys_i < params.getParameterGroup().size(); sys_i++) {

                // Database
                if (params.getParameterGroup().get(sys_i).getName().equals("DataBase")) {

                    log.info("   DataBase");

                    ParameterGroup GroupDataDase = params.getParameterGroup().get(sys_i);

                    for (int db_pg = 0; db_pg < GroupDataDase.getParameter().size(); db_pg++) {

                        Parameter ParamDB = GroupDataDase.getParameter().get(db_pg);

                        if (ParamDB.getName().equals("Driver")) {
                            DB_DRIVER = ParamDB.getValue();
                            log.info("      Driver=" + DB_DRIVER);
                        } else {
                            if (ParamDB.getName().equals("URL")) {
                                DB_URL = ParamDB.getValue();
                                log.info("      URL=" + DB_URL);
                            } else {
                                if (ParamDB.getName().equals("Host")) {
                                    DB_HOST = ParamDB.getValue();
                                    log.info("      Host=" + DB_HOST);
                                } else {
                                    if (ParamDB.getName().equals("DataBase")) {
                                        DB_PATH = ParamDB.getValue();
                                        log.info("      DataBase=" + DB_PATH);
                                    }
                                }
                            }
                        }

                        DB_URL = "jdbc:firebirdsql://" + DB_HOST + ":3050/" + DB_PATH;

                        log.info("      DB_URL=" + DB_URL);
                    }
                } else // TCP Server
                {
                    if (params.getParameterGroup().get(sys_i).getName().equals("Scheduler")) {

                        log.info("   Scheduler");

                        ParameterGroup GroupTCPServer = params.getParameterGroup().get(sys_i);

                        for (int db_pg = 0; db_pg < GroupTCPServer.getParameter().size(); db_pg++) {

                            Parameter ParamTCPServer = GroupTCPServer.getParameter().get(db_pg);

                            if (ParamTCPServer.getName().equals("WsURI")) {

                                WEB_SOCKET_URI = ParamTCPServer.getValue();
                                log.info("      WsURI=" + WEB_SOCKET_URI);

                            } else if (ParamTCPServer.getName().equals("WebServices")) {

                                WEB_SERVICES_URI = ParamTCPServer.getValue();
                                log.info("      WebServices=" + WEB_SERVICES_URI);

                            }

                        }

                    } else // TCP Server
                    {
                        if (params.getParameterGroup().get(sys_i).getName().equals("Labware")) {

                            log.info("   Labware");

                            ParameterGroup GroupLabware = params.getParameterGroup().get(sys_i);

                            for (ParameterGroup pg : GroupLabware.getParameterGroup()) {

                                PartitionTubeTypes pttlist = null;

                                for (Parameter param : pg.getParameter()) {

                                    if (param.getName().equals("Partition")) {
                                        pttlist = new PartitionTubeTypes(param.getValue());
                                        log.info("      Partition=" + param.getValue());

                                    } else if (param.getName().equals("TubeType")) {
                                        pttlist.getTubesTypes().add(new RackTubeType(param.getValue()));

                                        log.info("         TubeType=" + param.getValue());

                                    }

                                }

                                PartitionTubeTypesList.add(pttlist);

                            }

                        } else if (params.getParameterGroup().get(sys_i).getName().equals("1DScanner")) {

                            log.info("   1DScanner");

                            ParameterGroup Group1DScanner = params.getParameterGroup().get(sys_i);

                            for (Parameter param : Group1DScanner.getParameter()) {

                                if (param.getName().equals("Port")) {

                                    ManualScannerPort = param.getValue();
                                    log.info("      Port=" + ManualScannerPort);

                                }
                            }

                        }
                    }
                }

            }

        } catch (Exception E) {
            System.out.println(E.getMessage());
            log.info("Read Config file: " + PathToConf + ", Error: " + E.getMessage());
        }
    }

    private Device getDeviceById(String devId) {

        Device dev = null;

        for (int i = 0; i < Sys.getDevices().size(); i++) {

            if (((Device) Sys.getDevices().get(i)).getDevId().equals(devId)) {
                dev = (Device) Sys.getDevices().get(i);
                break;
            }

        }

        return dev;

    }

    public void ReadWSMessage(String MSG) {
        try {

            System.out.println(MSG);
            log.info("WS Message: " + MSG);

            JAXBContext jc = JAXBContext.newInstance(com.liconic.binding.sys.ObjectFactory.class);
            Unmarshaller u = jc.createUnmarshaller();

            StringReader reader = new StringReader(MSG);

            com.liconic.binding.sys.Sys mSys = (com.liconic.binding.sys.Sys) u.unmarshal(reader);

            for (com.liconic.binding.sys.Device mDev : mSys.getDevice()) {

                Device dev = getDeviceById(mDev.getDevId());

                if (dev != null) {

                    for (com.liconic.binding.sys.Status stat : mDev.getStatus()) {

                        // Init
                        if (stat.getId().equals("Busy")) {

                            if (stat.getValue().equals("true")) {
                                dev.setBusy(true);
                            } else {
                                dev.setBusy(false);
                            }

                        } else // Busy
                        {
                            if (stat.getId().equals("Init")) {

                                if (stat.getValue().equals("true")) {
                                    dev.setInit(true);
                                } else {
                                    dev.setInit(false);
                                }

                            } else // Error
                            {
                                if (stat.getId().equals("Error")) {

                                    if (stat.getValue().equals("true")) {
                                        dev.setError(true);
                                    } else {
                                        dev.setError(false);
                                    }

                                } else // ErrorCode
                                {
                                    if (stat.getId().equals("ErrorCode")) {

                                        dev.setErrorCode(Integer.valueOf(stat.getValue()));

                                    } else // Door
                                    {
                                        if (stat.getId().equals("DoorOpened")) {

                                            if (stat.getValue().equals("true")) {
                                                dev.setDoorOpened(true);
                                            } else {
                                                dev.setDoorOpened(false);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    dev.drawStatusGUI();

                }
            }

            if (mSys.getScheduler() != null) {

                Scheduler scheduler = mSys.getScheduler().getValue();

                DrawSchedulerStatus(scheduler.getStatus().getValue());

            }

            if (mSys.getCmds() != null) {

                Cmds cmds = mSys.getCmds().getValue();

                for (Cmd cmd : cmds.getCmd()) {

                    String CMDResult = "";
                    String CMDValue = "";

                    if (cmd.getResult() != null) {
                        CMDResult = cmd.getResult().getValue();
                    }

                    if (cmd.getValue() != null) {
                        CMDValue = cmd.getValue().getValue();
                    }

                    if ((!CMDResult.isEmpty()) || (!CMDValue.isEmpty())) {

                        if (importForm != null) {
                            importForm.SetCmdReply(Integer.valueOf(cmd.getId()), CMDResult, CMDValue);
                        }

                        if (importDoor != null) {
                            importDoor.SetCmdReply(Integer.valueOf(cmd.getId()), CMDResult, CMDValue);
                        }

                        if (exportRackStage != null) {
                            exportRackStage.SetCmdReply(Integer.valueOf(cmd.getId()), CMDResult, CMDValue);
                        }

                    }

                    DrawJobsContent(Integer.valueOf(cmd.getId()));

                }
            }

        } catch (Exception E) {
            System.out.println("Error Read Message: " + E.getMessage());
            log.error("Error Read Message: " + E.getMessage());
        }
    }

    public void DrawCountEmptyLevels() {

        final int emptyLevels = dm.getUserEmptyLevels(IdStoreDevice, user.getUserID());

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        lbEmptyLevels.setText("Empty levels: " + emptyLevels);

                    }
                });
            }

        }).start();

    }

    public void DrawCountEmptyRacks() {

        final int emptyRacks = dm.getUserEmptyRacks(IdStoreDevice, user.getUserID());

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        lbEmptyRacks.setText("Empty racks: " + emptyRacks);

                    }
                });

            }

        }).start();

    }

    public void DrawBuffer() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        TreeItem<BufferModel> BufferRootItem = bufferTableView.getRoot();

                        bufferOpennedNodes.clear();

                        for (TreeItem item : BufferRootItem.getChildren()) {

                            if (item.isExpanded()) {

                                if (((BufferModel) item.getValue()).getPosition().getClass().getName().equals("com.liconic.hardware.Cassette")) {
                                    bufferOpennedNodes.add(((Cassette) ((BufferModel) item.getValue()).getPosition()).getDBID());
                                }

                            }
                        }

                        bufferTableView.setRoot(dm.getBufferView(IdBufferDevice));

                        BufferRootItem = bufferTableView.getRoot();

                        for (int id : bufferOpennedNodes) {

                            for (TreeItem item : BufferRootItem.getChildren()) {

                                if (((BufferModel) item.getValue()).getPosition().getClass().getName().equals("com.liconic.hardware.Cassette")) {

                                    if (id == ((Cassette) ((BufferModel) item.getValue()).getPosition()).getDBID()) {
                                        item.setExpanded(true);
                                    }
                                }
                            }
                        }
                    }
                });
            }

        }).start();

    }

    public void DrawJobsContent(int id) {

        boolean isDraw = false;

        if (ImportRootItem != null) {

            for (TreeItem item : ImportRootItem.getChildren()) {

                if (((ImportTaskTableModel) item.getValue()).getId() == id) {

                    isDraw = true;
                    break;

                } else {

                    for (Object itemc : item.getChildren()) {

                        if (((ImportTaskTableModel) ((TreeItem) itemc).getValue()).getId() == id) {

                            isDraw = true;
                            break;

                        }
                    }
                }

            }

            if (isDraw) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {

                                ImportRootItem = dm.getImportTaskList(user.getUserID());
                                importTreeTableView.setRoot(ImportRootItem);
                                DrawTaskHistory(0);
                                DrawTaskProperty(0);

                            }
                        });
                    }

                }).start();
            }
        }

        isDraw = false;

        if (ExportRootItem != null) {

            for (TreeItem item : ExportRootItem.getChildren()) {

                if (((ExportTaskTableModel) item.getValue()).getIdTask() == id) {

                    isDraw = true;
                    break;

                } else {

                    for (Object itemc : item.getChildren()) {

                        if (((ExportTaskTableModel) ((TreeItem) itemc).getValue()).getIdTask() == id) {

                            isDraw = true;
                            break;

                        }
                    }
                }

            }

            if (isDraw) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {

                        Platform.runLater(new Runnable() {

                            @Override
                            public void run() {

                                ExportRootItem = dm.getExportTaskList(user.getUserID());
                                exportTreeTableView.setRoot(ExportRootItem);
                                DrawExpotTaskHistory(0);
                                DrawPickJobDetails(0);

                            }
                        });
                    }

                }).start();

            } else {

            }

        }
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setUserPassword(String UserPassword) {
        this.UserPassword = UserPassword;
    }

    public void setimportRackStage(ImportRackStage importRackStage) {
        this.importRackStage = importRackStage;
    }

    public boolean CheckBarcode(String BCR) {

        if (importRackStage == null) {

            new Thread(new Runnable() {

                @Override
                public void run() {

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {

                            importRackStage = new ImportRackStage(importStage, user, log, true);

                            importRackStage.initModality(Modality.WINDOW_MODAL);

                            importRackStage.setBarcode(BCR);

                            importRackStage.setLabware(PartitionTubeTypesList);

                            importRackStage.initOwner(scene.getWindow());

                            importRackStage.show();

                        }
                    });

                }

            }).start();

            return true;

        } else {
            return false;
        }

    }

    public void DrawImportTable() {
        ImportRootItem = dm.getImportTaskList(user.getUserID());
        importTreeTableView.setRoot(ImportRootItem);
    }

    public void DrawSchedulerStatus(String Status) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        lbSchedulerStatus.setText("Scheduler: " + Status);
                    }
                });

            }

        }).start();
    }

    public void setImportForm(ImportForm importForm) {
        this.importForm = importForm;
    }

    public void setImportDoor(ImportDoor importDoor) {
        this.importDoor = importDoor;
    }

    public void setExportRackStage(ExportRackStage exportRackStage) {
        this.exportRackStage = exportRackStage;
    }

    private void DrawTaskHistory(int id) {

        historyTaskTableData.clear();

        List<TaskHistoryModel> list = dm.getTaskHistory(id);

        for (TaskHistoryModel taskHistoryModel : list) {
            historyTaskTableData.add(taskHistoryModel);
        }

        importHistoryTableView.setItems(historyTaskTableData);

    }

    private void DrawTaskProperty(int id) {

        propertyTaskTableData.clear();

        List<TaskPropertyModel> list = dm.getTaskProperty(id);

        for (TaskPropertyModel taskPropertyModel : list) {
            propertyTaskTableData.add(taskPropertyModel);
        }

        importPropertiesTableView.setItems(propertyTaskTableData);

    }

    private void DrawPickJobDetails(int id) {

        titledPaneExportDetails.setContent(null);

        exportContentTable.getItems().clear();

        List<ExportTubePickModel> list = dm.getPickJobDetails(id);

        ObservableList<ExportTubePickModel> propertyExportTask = FXCollections.observableArrayList();

        for (ExportTubePickModel etpm : list) {
            propertyExportTask.add(etpm);
        }

        exportContentTable.setItems(propertyExportTask);

        titledPaneExportDetails.setContent(exportContentTable);

    }

    private void DrawPickJobExportDetails(int id) {

        titledPaneExportDetails.setContent(null);

        exportRacksTable.getItems().clear();

        List<ExportPlateModel> list = dm.getExportPlateDetails(id);

        propertyExportTask.clear();

        for (ExportPlateModel etpm : list) {
            propertyExportTask.add(etpm);
        }

        exportRacksTable.setItems(propertyExportTask);

        titledPaneExportDetails.setContent(exportRacksTable);

    }

    private TableView CreateimportHistoryTableView() {

        TableColumn statusColumn;
        TableColumn dateColumn;
        TableColumn userColumn;
        TableColumn noteColumn;

        statusColumn = new TableColumn("Status");
        statusColumn.setEditable(false);
        statusColumn.setPrefWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("status"));

        dateColumn = new TableColumn("Date");
        dateColumn.setEditable(false);
        dateColumn.setPrefWidth(150);
        dateColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("date"));

        userColumn = new TableColumn("User");
        userColumn.setEditable(false);
        userColumn.setPrefWidth(100);
        userColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("user"));

        noteColumn = new TableColumn("Note");
        noteColumn.setEditable(false);
        noteColumn.setPrefWidth(200);
        noteColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("note"));

        importHistoryTableView = new TableView();

        importHistoryTableView.setPlaceholder(new Text(""));

        importHistoryTableView.getColumns().addAll(statusColumn, dateColumn, userColumn, noteColumn);

        return importHistoryTableView;
    }

    private TableView CreateimportPropertiesTableView() {

        TableColumn propertyColumn;
        TableColumn valueColumn;

        propertyColumn = new TableColumn("Property");
        propertyColumn.setEditable(false);
        propertyColumn.setPrefWidth(150);
        propertyColumn.setCellValueFactory(new PropertyValueFactory<TaskPropertyModel, String>("property"));

        valueColumn = new TableColumn("Value");
        valueColumn.setEditable(false);
        valueColumn.setPrefWidth(100);
        valueColumn.setCellValueFactory(new PropertyValueFactory<TaskPropertyModel, String>("value"));

        importPropertiesTableView = new TableView();
        importPropertiesTableView.setPlaceholder(new Text(""));

        importPropertiesTableView.getColumns().addAll(propertyColumn, valueColumn);

        return importPropertiesTableView;

    }
 private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private TableView CreateExportHistoryTableView() {

        TableColumn statusColumn;
        TableColumn dateColumn;
        TableColumn userColumn;
        TableColumn noteColumn;

        statusColumn = new TableColumn("Status");
        statusColumn.setEditable(false);
        statusColumn.setPrefWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("status"));

        dateColumn = new TableColumn("Date");
        dateColumn.setEditable(false);
        dateColumn.setPrefWidth(150);
        dateColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("date"));

        userColumn = new TableColumn("User");
        userColumn.setEditable(false);
        userColumn.setPrefWidth(100);
        userColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("user"));

        noteColumn = new TableColumn("Note");
        noteColumn.setEditable(false);
        noteColumn.setPrefWidth(200);
        noteColumn.setCellValueFactory(new PropertyValueFactory<TaskHistoryModel, String>("note"));

        exportHistoryTable = new TableView();

        exportHistoryTable.setPlaceholder(new Text(""));

        exportHistoryTable.getColumns().addAll(statusColumn, dateColumn, userColumn, noteColumn);

        return exportHistoryTable;
    }

    private TreeTableView CreateExportTable() {

        TreeTableColumn<ExportTaskTableModel, String> idColumn;

        TreeTableColumn taskColumn;

        TreeTableColumn statusColumn;

        TreeTableColumn noteExportColumn;
        

        idColumn = new TreeTableColumn<>("Id");
        idColumn.setEditable(false);
        idColumn.setMinWidth(80);
        idColumn.setCellValueFactory(new TreeItemPropertyValueFactory("jobId"));

        idColumn.setCellFactory(new Callback<TreeTableColumn<ExportTaskTableModel, String>, TreeTableCell<ExportTaskTableModel, String>>() {

            @Override
            public TreeTableCell<ExportTaskTableModel, String> call(TreeTableColumn<ExportTaskTableModel, String> param) {

                TreeTableCell<ExportTaskTableModel, String> cell = new TreeTableCell<ExportTaskTableModel, String>() {

                    @Override

                    public void updateItem(String object, boolean empty) {

                        if (!empty) {

                            if (object != null) {

                                HBox box = new HBox();

                                box.setSpacing(10);

                                VBox vbox = new VBox();

                                String caption = "";

                                if (object != null) {

                                    caption = object;

                                } else {
                                    caption = "";
                                }

                                vbox.getChildren().add(new Label(caption));

                                box.getChildren().addAll(vbox);

                                setGraphic(box);

                            } else {
                                HBox box = new HBox();
                                setGraphic(box);
                            }
                        } else {

                            setGraphic(null);
                            setText(null);
                        }
                    }
                };

                return cell;

            }

        });

        taskColumn = new TreeTableColumn<>("Task");
        taskColumn.setEditable(false);
        taskColumn.setMinWidth(100);
        taskColumn.setCellValueFactory(new TreeItemPropertyValueFactory("task"));
        taskColumn.setCellFactory(new ExportTaskCellFactory());

        statusColumn = new TreeTableColumn<>("Status");
        statusColumn.setEditable(false);
        statusColumn.setMinWidth(90);
        statusColumn.setCellValueFactory(new TreeItemPropertyValueFactory("status"));
        statusColumn.setCellFactory(new ExportTaskCellFactory());

        noteExportColumn = new TreeTableColumn<>("Note");
        noteExportColumn.setEditable(false);
        noteExportColumn.setMinWidth(200);
        noteExportColumn.setCellValueFactory(new TreeItemPropertyValueFactory("note"));
        noteExportColumn.setCellFactory(new ExportTaskCellFactory());
        
        ExportRootItem = dm.getExportTaskList(user.getUserID());

        exportTreeTableView = new TreeTableView(ExportRootItem);
       
          
        exportTreeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem>() {

            @Override
            public void changed(ObservableValue<? extends TreeItem> paramObservableValue, TreeItem paramT1, TreeItem selectedItem) {

                if (selectedItem != null) {

                    System.out.println(((ExportTaskTableModel) selectedItem.getValue()).getTask() + " - " + ((ExportTaskTableModel) selectedItem.getValue()).getIdTask());
                   
                    if (((ExportTaskTableModel) selectedItem.getValue()).getIdTask() > 0) {

                        DrawExpotTaskHistory(((ExportTaskTableModel) selectedItem.getValue()).getIdTask());

                        if (((ExportTaskTableModel) selectedItem.getValue()).getTask().equals("Pick Tubes")) {
                            DrawPickJobDetails(((ExportTaskTableModel) selectedItem.getValue()).getIdTask());
                        } else {
                            DrawPickJobExportDetails(((ExportTaskTableModel) selectedItem.getValue()).getIdTask());
                        }

                    }
                }
            }

        });

        exportTreeTableView.setShowRoot(false);
        exportTreeTableView.setEditable(false);
        exportTreeTableView.setPlaceholder(new Text(""));
        exportTreeTableView.getStylesheets().add("style/stylesTaskTree.css");
       
        exportTreeTableView.getColumns().addAll(idColumn, taskColumn, statusColumn, noteExportColumn);

        MenuItem menuCancelExport = new MenuItem("Cancel");
        MenuItem menuContinue = new MenuItem("Continue");
        MenuItem menusuperP = new MenuItem("Run Now");
        Fnclient fn = new Fnclient(WEB_SERVICES_URI, log);
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem menuSequenceExport = new MenuItem("Set Sequence");
     
        
        
       menuCancelExport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                if (((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob() != 0) {

                    try {

                        ExportClient exportClient = new ExportClient(WEB_SERVICES_URI, log);
                        Sys sys = exportClient.canceExportRack(((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob(), UserName);

                        Cmd cmd = sys.getCmds().getValue().getCmd().get(0);

                        if ((cmd.getStatus() != null) && (cmd.getStatus().getValue().equals("Error"))) {

                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error of cancelling the Export Job");
                            alert.setContentText(cmd.getResult().getValue());

                            alert.showAndWait();

                        } else if ((cmd.getStatus() != null) && (cmd.getStatus().getValue().equals("Ok"))) {

                            ExportRootItem.getChildren().remove((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem());

                        }

                    } catch (Exception E) {

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error of cancelling the Export Job");
                        alert.setContentText(E.getMessage());

                        alert.showAndWait();

                    }

                }

            }
        });

        ContextMenu contextMenuCancelTask = new ContextMenu(menuCancelExport, separatorMenuItem, menuSequenceExport,menuContinue, menusuperP);
       // contextMenuCancelTask.getItems().add(menuCancelExport);
      //  contextMenuCancelTask.getItems().add(menuContinue);
      //  contextMenuCancelTask.getItems().add(menusuperP);
        
		
contextMenuCancelTask.setOnShowing(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {

              if (((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob() != 0) {  
                  int jobId = ((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob();
                    
                    menuCancelExport.setDisable(false);
                    separatorMenuItem.setDisable(false);
                    
                    if(dm.canSetSequence(jobId)){
                        
                        menuSequenceExport.setDisable(false);
                                                    
                    }else{
                        menuSequenceExport.setDisable(true);
						
                }
              }else {
                    menuCancelExport.setDisable(true);
                }
				  
                  
                    if (((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob() != 0) {
                        int idJob = ((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob();
                        String status = dm.getStatus(idJob);
                    if (status.equals("Waiting")){ 
                        menusuperP.setDisable(false);
                    }
                    else{
                          menusuperP.setDisable(true);
                    }
               }
			   else {
                    menuCancelExport.setDisable(true);
                    separatorMenuItem.setDisable(true);
                    menuSequenceExport.setDisable(true);
			   }
                    
                    
                if (((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob() != 0) {
                   
                        
                        int v = -1;
                        int id = ((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob();
                        int taskId = dm.getTaskId(id);
                           
                      try {
                          
                       v  = fn.checkContinue(taskId);                          
                      } catch (Exception ex) {
                        System.out.println("Check continue eligibility: " + ex.getMessage());
                      }
                      
                      if(v == 1){
                          menuContinue.setDisable(false);
                      }
                      else
                          menuContinue.setDisable(true);                     
                    }
                else
                      menuContinue.setDisable(true);
            }
                });
        menuContinue.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                if (((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob() != 0) {

                    try {

                        Fnclient fn = new Fnclient(WEB_SERVICES_URI, log);
                      int status = fn.continuePick(((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdTask());

                      if(status != 200){
                          
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error of continue Pick job");
                            alert.setContentText("Status:"+status);

                            alert.showAndWait();

                        } else {
                          
                      }

                    } catch (Exception E) {

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error of continue Pick job");
                        alert.setContentText(E.getMessage());

                        alert.showAndWait();

                    }
                }

            }
        });
		
		  menuSequenceExport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                if (((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob() != 0) {

                    try {
																																																																												  

                        int jobId = ((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob();

                        int sequence = dm.getJobSequence(jobId);
                                                
                        TextInputDialog dialog = new TextInputDialog(String.valueOf(sequence));
                        dialog.setTitle("Pick Job Sequence");
                        dialog.setHeaderText("Enter a Pick Job Sequence");
                        dialog.setContentText("Sequence:");

                        // Traditional way to get the response value.
                        Optional<String> result = dialog.showAndWait();
                        
                        if (result.isPresent()){
                            
                            sequence = Integer.valueOf(result.get());
                            
                            SchedulerClient client = new SchedulerClient(WEB_SERVICES_URI, log);
                            
                            client.setSequence(jobId, sequence);
                        }
     

						  

                    } catch (Exception E) {

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Error of set sequence");
                        alert.setContentText(E.getMessage());

                        alert.showAndWait();

                    }

                }


            }
        });        
        
        
     menusuperP.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                   if (((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob() != 0) {

                    try {
                        int status = fn.superP(((ExportTaskTableModel) ((TreeItem) exportTreeTableView.getSelectionModel().getSelectedItem()).getValue()).getIdJob(), UserName);

                       if(status != 200){

                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Super Priority");
                            alert.setContentText("Error of running the Super priority Job");

                            alert.showAndWait();

                        } 

                    } catch (Exception E) {

                       Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Error of running the Super priority Job");
                            alert.setContentText(E.getMessage());

                            alert.showAndWait();

                    }

                }


            }
        });
             
             
    

        exportTreeTableView.setContextMenu(contextMenuCancelTask);

        return exportTreeTableView;

    }
    

    private TableView CreateExportContentTable() {

        TableColumn srcTubeBCColumn;
        TableColumn srcXColumn;
        TableColumn srcYColumn;
        TableColumn srcRackBCColumn;
        TableColumn trgRackBCColumn;
        TableColumn trgXColumn;
        TableColumn trgYColumn;
        TableColumn statusColumn;
        TableColumn noteExportColumn;

        srcTubeBCColumn = new TableColumn("Source Sample");
        srcTubeBCColumn.setEditable(false);
        srcTubeBCColumn.setPrefWidth(120);
        srcTubeBCColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("srcTubeBC"));

        srcXColumn = new TableColumn("Source X");
        srcXColumn.setEditable(false);
        srcXColumn.setPrefWidth(70);
        srcXColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("srcX"));

        srcYColumn = new TableColumn("Source Y");
        srcYColumn.setEditable(false);
        srcYColumn.setPrefWidth(70);
        srcYColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("srcY"));

        srcRackBCColumn = new TableColumn("Source Rack");
        srcRackBCColumn.setEditable(false);
        srcRackBCColumn.setPrefWidth(120);
        srcRackBCColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("srcRackBC"));

        trgRackBCColumn = new TableColumn("Target Rack");
        trgRackBCColumn.setEditable(false);
        trgRackBCColumn.setPrefWidth(120);
        trgRackBCColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("trgRackBC"));

        trgXColumn = new TableColumn("Target X");
        trgXColumn.setEditable(false);
        trgXColumn.setPrefWidth(70);
        trgXColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("trgX"));

        trgYColumn = new TableColumn("Target Y");
        trgYColumn.setEditable(false);
        trgYColumn.setPrefWidth(70);
        trgYColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("trgY"));

        statusColumn = new TableColumn("Status");
        statusColumn.setEditable(false);
        statusColumn.setPrefWidth(70);
        statusColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("status"));

        noteExportColumn = new TableColumn("Note");
        noteExportColumn.setEditable(false);
        noteExportColumn.setPrefWidth(170);
        noteExportColumn.setCellValueFactory(new PropertyValueFactory<ExportTubePickModel, String>("note"));

        exportContentTable = new TableView();
        exportContentTable.setPlaceholder(new Text(""));

        exportContentTable.getColumns().addAll(srcTubeBCColumn, srcXColumn, srcYColumn, srcRackBCColumn, trgRackBCColumn, trgXColumn, trgYColumn, statusColumn, noteExportColumn);

        return exportContentTable;

    }

    private TableView CreateExportRacksTable() {

        TableColumn plateBCColumn;
        TableColumn deviceColumn;
        TableColumn partitionColumn;
        TableColumn cassetteColumn;
        TableColumn levelColumn;

        plateBCColumn = new TableColumn("Barcode");
        plateBCColumn.setEditable(false);
        plateBCColumn.setPrefWidth(150);
        plateBCColumn.setCellValueFactory(new PropertyValueFactory<ExportPlateModel, String>("plateBCR"));

        deviceColumn = new TableColumn("Device");
        deviceColumn.setEditable(false);
        deviceColumn.setPrefWidth(100);
        deviceColumn.setCellValueFactory(new PropertyValueFactory<TaskPropertyModel, String>("device"));

        partitionColumn = new TableColumn("Partition");
        partitionColumn.setEditable(false);
        partitionColumn.setPrefWidth(100);
        partitionColumn.setCellValueFactory(new PropertyValueFactory<TaskPropertyModel, String>("partition"));

        cassetteColumn = new TableColumn("Cassette");
        cassetteColumn.setEditable(false);
        cassetteColumn.setPrefWidth(100);
        cassetteColumn.setCellValueFactory(new PropertyValueFactory<TaskPropertyModel, String>("cassette"));

        levelColumn = new TableColumn("Level");
        levelColumn.setEditable(false);
        levelColumn.setPrefWidth(100);
        levelColumn.setCellValueFactory(new PropertyValueFactory<TaskPropertyModel, String>("level"));

        exportRacksTable = new TableView();
        exportRacksTable.setPlaceholder(new Text(""));

        exportRacksTable.getColumns().addAll(plateBCColumn, deviceColumn, partitionColumn, cassetteColumn, levelColumn);

        MenuItem menuRunExport = new MenuItem("Export Rack");
       // MenuItem menuExportJob = new MenuItem("Export Job");
        menuRunExport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                ExportPlateModel item = propertyExportTask.get(exportRacksTable.getSelectionModel().getSelectedIndex());

                if (item != null) {

                    System.out.println(item.getIdTask() + " " + item.getDevice());

                    exportRackStage = new ExportRackStage(importStage, WEB_SERVICES_URI, item.getIdTask(), 0, log);
                    exportRackStage.initModality(Modality.WINDOW_MODAL);
                    exportRackStage.initOwner(scene.getWindow());
                    exportRackStage.show();
                }

            }
        });

 /*       menuExportJob.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                ExportPlateModel item = propertyExportTask.get(exportRacksTable.getSelectionModel().getSelectedIndex());

                if (item != null) {

                    System.out.println(item.getIdTask() + " " + item.getDevice());

                    exportRackStage = new ExportRackStage(importStage, WEB_SERVICES_URI, item.getIdTask(), 0, log);
                    exportRackStage.initModality(Modality.WINDOW_MODAL);
                    exportRackStage.initOwner(scene.getWindow());
                    exportRackStage.show();
                }

            }
        });*/
        
        ContextMenu contextExport = new ContextMenu();
    //    contextExport.getItems().add(menuExportJob);
        contextExport.getItems().add(menuRunExport);
        exportRacksTable.setContextMenu(contextExport);
//        exportRacksTable.setContextMenu(new ContextMenu(menuRunExport));
        return exportRacksTable;

    }

    public void DrawImportTask(int id) {

        System.out.println("ID Task = " + id);

        ObservableList<SplitPane.Divider> dividers = SplitPaneImport.getDividers();

        if (dividers.size() == 1) {

            dividerTask = dividers.get(0).getPosition();

        } else {
            dividerTask = dividers.get(0).getPosition();
            dividerHistory = dividers.get(1).getPosition();
        }

        SplitPaneImport.getItems().clear();
        SplitPaneImport.getItems().addAll(importTreeTableView, titledPaneImportProperties, titledPaneImportHistory);

        SplitPaneImport.setDividerPosition(0, dividerTask);
        SplitPaneImport.setDividerPosition(1, dividerHistory);

        DrawTaskHistory(id);
        DrawTaskProperty(id);

    }

    public void DrawImportJob(int idJob, int IdPlate) {

        System.out.println("ID Job = " + idJob);

        ObservableList<SplitPane.Divider> dividers = SplitPaneImport.getDividers();

        if (dividers.size() == 1) {

            dividerTask = dividers.get(0).getPosition();

        } else {
            dividerTask = dividers.get(0).getPosition();
            dividerHistory = dividers.get(1).getPosition();
        }

        SplitPaneImport.getItems().clear();
        SplitPaneImport.getItems().addAll(importTreeTableView, titledPaneImportProperties);

        SplitPaneImport.setDividerPosition(0, dividerTask);

        propertyTaskTableData.clear();

        List<TaskPropertyModel> list = dm.getJobProperty(idJob, IdPlate);

        for (TaskPropertyModel taskPropertyModel : list) {
            propertyTaskTableData.add(taskPropertyModel);
        }

        importPropertiesTableView.setItems(propertyTaskTableData);

    }

    public void DrawExportTask(int id) {

        DrawTaskHistory(id);

    }

    public void DrawExportPickTask(int id) {

        System.out.println("ID Task = " + id);

        ObservableList<SplitPane.Divider> dividers = SplitPaneImport.getDividers();

        if (dividers.size() == 1) {

            dividerTask = dividers.get(0).getPosition();

        } else {
            dividerTask = dividers.get(0).getPosition();
            dividerHistory = dividers.get(1).getPosition();
        }

        SplitPaneImport.getItems().clear();
        SplitPaneImport.getItems().addAll(importTreeTableView, titledPaneImportProperties, titledPaneImportHistory);

        SplitPaneImport.setDividerPosition(0, dividerTask);
        SplitPaneImport.setDividerPosition(1, dividerHistory);

        DrawTaskHistory(id);
        DrawTaskProperty(id);

    }

    private void DrawExpotTaskHistory(int id) {

        historyExportTaskTableData.clear();
        exportHistoryTable.getItems().clear();

        List<TaskHistoryModel> list = dm.getTaskHistory(id);

        for (TaskHistoryModel taskHistoryModel : list) {
            historyExportTaskTableData.add(taskHistoryModel);
        }

        exportHistoryTable.setItems(historyExportTaskTableData);

    }

    public void ConnectToWebsocket() {

        try {

            clientEndPoint = new WebsocketClientEndpoint(this, new URI(WEB_SOCKET_URI), log);

            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {

                public void handleMessage(final String message) {

                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    ReadWSMessage(message);

                                }
                            });

                        }

                    }).start();

                }

            });
        } catch (Exception E) {

            clientEndPoint = null;

            System.out.println("Error Openning Web Socket: " + E.getMessage());

            StageMessage stageMessage = new StageMessage(2, "Communication Error", "Can not connect to Scheduler!");
            stageMessage.initModality(Modality.WINDOW_MODAL);
            stageMessage.initOwner(scene.getWindow());
            stageMessage.show();

        }

    }

    public void setDisconnectWS() {

        clientEndPoint = null;

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            for (Device dev : Sys.getDevices()) {

                                dev.setError(false);
                                dev.setInit(false);

                                dev.drawStatusGUI();
                            }

                        } catch (Exception E) {
                            System.out.println("Error draw device status: " + E.getMessage());
                        }

                        if (IsLogout) {
                            return;
                        }

                        StageMessage stageMessage = new StageMessage(2, "Communication Error", "Connection with Scheduler is broken!");
                        stageMessage.initModality(Modality.WINDOW_MODAL);
                        stageMessage.initOwner(scene.getWindow());

                        stageMessage.show();

                    }

                });

            }

        }).start();

    }

    public TableView CreateExceptionTable() {

        exceptionTable = new TableView();
        exceptionTable.setPlaceholder(new Text(""));

        TableColumn pltExcBCColumn;
        TableColumn pltExcCassetteColumn;
        TableColumn pltExcLevelColumn;

        pltExcBCColumn = new TableColumn("Barcode");
        pltExcBCColumn.setEditable(false);
        pltExcBCColumn.setPrefWidth(120);
        pltExcBCColumn.setCellValueFactory(new PropertyValueFactory<ExportExceptioRacksModel, String>("plateBCR"));

        pltExcCassetteColumn = new TableColumn("Cassette");
        pltExcCassetteColumn.setEditable(false);
        pltExcCassetteColumn.setPrefWidth(90);
        pltExcCassetteColumn.setCellValueFactory(new PropertyValueFactory<ExportExceptioRacksModel, Integer>("cassette"));

        pltExcLevelColumn = new TableColumn("Level");
        pltExcLevelColumn.setEditable(false);
        pltExcLevelColumn.setPrefWidth(90);
        pltExcLevelColumn.setCellValueFactory(new PropertyValueFactory<ExportExceptioRacksModel, Integer>("level"));

        exceptionTable.getColumns().addAll(pltExcBCColumn, pltExcCassetteColumn, pltExcLevelColumn);

        MenuItem ExportRack = new MenuItem("Export");

        ExportRack.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {

                System.out.println(((ExportExceptioRacksModel) exceptionTable.getSelectionModel().getSelectedItem()).getIdPlate());

                exportRackStage = new ExportRackStage(importStage, WEB_SERVICES_URI, 0, ((ExportExceptioRacksModel) exceptionTable.getSelectionModel().getSelectedItem()).getIdPlate(), log);
                exportRackStage.initModality(Modality.WINDOW_MODAL);
                exportRackStage.initOwner(scene.getWindow());
                exportRackStage.show();

            }
        });

        exceptionTable.setContextMenu(new ContextMenu(ExportRack));

        return exceptionTable;

    }

    public void DrawExceptionPlates() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        exceptionTable.getItems().clear();
                        exceptionTable.setItems(dm.getExceptionRacks());
                    }
                });
            }

        }).start();

    }

    public void setErrorWS(String MSG) {

        StageMessage stageMessage = new StageMessage(2, "Communication Error", MSG);
        stageMessage.initModality(Modality.WINDOW_MODAL);
        stageMessage.initOwner(scene.getWindow());

        stageMessage.show();

    }

    public void DrawWarmupDelay() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        if (dBTimer != null) {

                            String WarmUpDely = dBTimer.getWarupDelay();

                            if (!WarmUpDely.isEmpty()) {
                                lbWarmUpTime.setText("Pending task: " + WarmUpDely);
                                log.info("Pending task: " + WarmUpDely);
                            } else {
                                lbWarmUpTime.setText("");
                            }

                        }

                    }

                });

            }

        }).start();

    }

    public void setdBTimer(String DBdriver, String DBpath, String DBuser, String DBpassword, Logger log) {

        if (warmUpTimer == null) {

            try {

                this.dBTimer = new DBTimer(DBdriver, DBpath, DBuser, DBpassword, log);

                warmUpTimer = new WarmUpTimer(importStage);

            } catch (Exception E) {

            }
        }
    }

    public void LogOut() {

        log.info("LogOut");

        IsLogout = true;

        user = null;
        //Sys = null;

        if (en != null) {
            en.stop();
            en = null;
        }

        if (clientEndPoint != null) {
            clientEndPoint.Close();
            clientEndPoint = null;
        }

        if (BCReader != null) {
            BCReader.ClosePort();
            BCReader = null;
        }

        if (warmUpTimer != null) {

            warmUpTimer.Stop();
            warmUpTimer = null;

        }

        if (logOutTimer != null) {
            logOutTimer.Stop();
            logOutTimer = null;
        }

        lbSchedulerStatus.setText("");

        lbEmptyLevels.setText("");

        lbEmptyRacks.setText("");

        lbWarmUpTime.setText("");

        ImportRootItem = null;
        importTreeTableView.setRoot(ImportRootItem);

        bufferTableView.setRoot(null);

        LoginStage ls = new LoginStage(log);

        ls.setDB_DRIVER(DB_DRIVER);
        ls.setDB_URL(DB_URL);
        ls.setImportStage(this);

        ls.initModality(Modality.WINDOW_MODAL);
        ls.initOwner(scene.getWindow());

        ls.showAndWait();

        if (user == null) {

            Platform.exit();

        } else {

            IsLogout = false;

            if (user.getUserRole() == 1) {

            }

//            logOutTimer = new LogOutTimer(importStage);
            ConnectToWebsocket();

            if (!ManualScannerPort.isEmpty()) {

                try {
                    BCReader = new DataLogicBCReader(ManualScannerPort, this, log);
                } catch (Exception E) {

                    JOptionPane.showMessageDialog(null,
                            "Error of running manual BC Reader, Port: " + ManualScannerPort + "!",
                            "Error",
                            JOptionPane.WARNING_MESSAGE);

                    BCReader = null;

                }

            }

            DrawImportTable();

            dm.getUserLabware(PartitionTubeTypesList, user.getUserID());

            bufferTableView.setRoot(dm.getBufferView(IdBufferDevice));

            DrawCountEmptyLevels();
            DrawCountEmptyRacks();

            en = new DBEventNotifier(DB_HOST, DB_PATH, UserName, UserPassword, this, log);

            en.run();
            /*            
            try{
                
                clientEndPoint = new WebsocketClientEndpoint(new URI(WEB_SOCKET_URI));
                
                clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                
                public void handleMessage(final String message) {
                    
                    new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    ReadWSMessage(message);

                                }
                            });

                        }

                    }).start();                    
                    
                    
                    
                }
                
            });                                
            }catch(Exception E){
                System.out.println("Error Openning Web Socket: "+E.getMessage());
                
                StageMessage stageMessage = new StageMessage(true, "Communikation Error", "Can not connect to Scheduler!");
                stageMessage.initModality(Modality.WINDOW_MODAL);
                stageMessage.initOwner(scene.getWindow());
                stageMessage.show();
            }
             */

        }

    }

    public void UpdateContentGUI() {

        // Buffer
        DrawBuffer();

        // Exception racks
        DrawExceptionPlates();

        // Import
        ImportRootItem = dm.getImportTaskList(user.getUserID());
        importTreeTableView.setRoot(ImportRootItem);
        DrawTaskHistory(0);
        DrawTaskProperty(0);

        // Export
        ExportRootItem = dm.getExportTaskList(user.getUserID());
        exportTreeTableView.setRoot(ExportRootItem);
        DrawExpotTaskHistory(0);
        DrawPickJobDetails(0);

    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void CheckLogOut() {
        /*        
        if (IsLogout)
            return;
        
        new Thread(new Runnable() {

            @Override
            public void run() {

                Platform.runLater(new Runnable() {
                
                    @Override
                    public void run() {
                        
                        System.out.println("Check log out");
                        
                        Date date = new Date();
                        
                        System.out.println("Check log out: "+ ((lastAccessDate.getTime() + 3*60 *1000)  -  date.getTime()));
                        
                        if((lastAccessDate.getTime() + 3*60 *1000) <= date.getTime()){
                            System.out.println("Log Out!");
                            log.info("Log Out Timer");
                            LogOut();
                        }
                        
                    }
                    
                });

            }

        }).start();        
         */
    }

}
