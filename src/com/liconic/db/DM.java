package com.liconic.db;

import com.liconic.labware.Plate;
import com.liconic.buffergui.BufferModel;
import com.liconic.hardware.Cassette;
import com.liconic.hardware.Device;
import com.liconic.hardware.KIWISystem;
import com.liconic.hardware.Level;
import com.liconic.labware.configuration.PartitionTubeTypes;
import com.liconic.labware.configuration.RackTubeType;
import com.liconic.table.exporttasks.ExportExceptioRacksModel;
import com.liconic.table.exporttasks.ExportPlateModel;
import com.liconic.table.exporttasks.ExportTaskTableModel;
import com.liconic.table.exporttasks.ExportTubePickModel;
import com.liconic.table.importtasks.ImportTaskTableModel;
import com.liconic.table.importtasks.TaskHistoryModel;
import com.liconic.table.importtasks.TaskPropertyModel;
import com.liconic.table.settings.SettingsTableRecord;
import com.liconic.user.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import org.apache.logging.log4j.Logger;

public class DM {

    private Logger log;
    private String DBpath;
    private String DBuser;
    private String DBpassword;
    int count = 0;

    private DateFormat df = new SimpleDateFormat("HH:mm MM.dd.yyyy");

    public DM(String DBdriver, String DBpath, String DBuser, String DBpassword, Logger log) throws Exception {
        this.DBpath = DBpath;
        this.DBuser = DBuser;
        this.DBpassword = DBpassword;
        this.log = log;

        Class.forName(DBdriver);
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(DBpath, DBuser, DBpassword);
        } catch (SQLException E) {
            System.err.println("101: " + E.getMessage() + "  " + E.getSQLState());
            log.error("Get DB connection: " + E.getMessage() + "  " + E.getSQLState());
            return null;
        }
    }

    public synchronized User getUser(String Login) throws SQLException {

        User user = null;

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet resultset = null;

        Connection connection;

        try {

            connection = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_user, user_name, user_second_name, user_login, user_e_mail, user_role \n" +
"                 FROM users \n" +
"              WHERE UPPER(user_login)=? AND user_deleted=0";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.setString(1, Login.toUpperCase());

            resultset = prepstat.executeQuery();

            while (resultset.next()) {

                user = new User(resultset.getInt(1),
                        resultset.getString("user_name"),
                        resultset.getInt("user_role"),
                        resultset.getString("user_second_name"),
                        resultset.getString("user_login"),
                        resultset.getString("user_e_mail"),
                        1);

                break;

            }

            resultset.close();
            prepstat.close();
            connection.close();

            return user;

        } catch (SQLException E) {

            log.error("getUser: " + E.getMessage() + "  " + E.getSQLState() + ", Login: " + Login);
            throw E;

        }

    }

    public KIWISystem getSystem() {

        KIWISystem system = null;

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet rs = null;

        Connection conn;

        try {

            conn = getConnection();

            SQLVal = "SELECT id_system, system_id FROM systems";

            prepstat = conn.prepareStatement(SQLVal);

            rs = prepstat.executeQuery();

            while (rs.next()) {

                if (system == null) {
                    system = new KIWISystem(rs.getInt("id_system"), rs.getString("system_id"));
                }
                
            }
            
            rs.close();
            prepstat.close();
            
            SQLVal = "SELECT id_device, device_id from devices";

            prepstat = conn.prepareStatement(SQLVal);

            rs = prepstat.executeQuery();

             while (rs.next()) {
                Device devs = new Device(rs.getInt("id_device"), rs.getString("device_id"));

                system.AddDevice(devs);
             }

            rs.close();
            prepstat.close();
        

            for (Device dev : system.getDevices()) {

                SQLVal = "SELECT param_name\n" +
"                       FROM param_link_device, device_params \n" +
"                       WHERE link_device=? AND link_param=ID_PARAM_LINK_DEVICE";

                prepstat = conn.prepareStatement(SQLVal);

                prepstat.setInt(1, dev.getId());

                rs = prepstat.executeQuery();

                while (rs.next()) {

                    System.out.println(rs.getString("param_name"));

                    if (rs.getString("param_name").equals("Door")) {
                        dev.setDoor(true);
                    } else if (rs.getString("param_name").equals("IO")) {
                        dev.setSTB(true);
                    } else if (rs.getString("param_name").equals("STT")) {
                        dev.setSTT(true);
                    } else if (rs.getString("param_name").equals("Storage")) {
                        dev.setSTC(true);
                    }
                }
            }

            conn.close();

        } catch (SQLException E) {
            log.error("getSystem: " + E.getMessage());
        }

        return system;
    }

    public int getUserEmptyLevels(int IdDevice, int idUser) {

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet rs = null;

        Connection conn;

        int res = 0;

        try {

            conn = getConnection();

            SQLVal = "SELECT COUNT(DISTINCT id_level) FROM levels L, cassettes, cassette_link_partition, user_link_partition, "
                    + "partitions WHERE cassette_device=? "
                    + "AND cassette_link_partition.link_partition=id_partition AND link_cassette=id_cassette "
                    + "AND user_link_partition.link_partition=id_partition AND link_user=? "
                    + "AND level_cassette=id_cassette AND link_plate=NULL";

            prepstat = conn.prepareStatement(SQLVal);

            prepstat.setInt(1, IdDevice);
            prepstat.setInt(2, idUser);

            rs = prepstat.executeQuery();

            while (rs.next()) {

                res = rs.getInt(1);
                break;
            }

            rs.close();
            conn.close();

        } catch (Exception E) {
            System.out.println(E.getMessage());
            log.error("getUserEmptyLevels: " + E.getMessage());
        }

        return res;
    }

    public int getUserEmptyRacks(int IdDevice, int idUser) {

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet rs = null;

        Connection conn;

        int res = 0;

        try {
            conn = getConnection();

            SQLVal = "SELECT count(DISTINCT id_plate) FROM cassette_link_partition, cassettes, levels, plates P, plate_link_tube_pos,"
                    + " user_link_partition, partitions "
                    + "WHERE cassette_device="+IdDevice+" AND cassette_link_partition.link_partition=id_partition "
                    + "AND link_cassette=id_cassette AND user_link_partition.link_partition=id_partition "
                    + "AND link_user="+idUser +" AND level_cassette=id_cassette AND levels.link_plate=id_plate "
                    + "AND plate_link_tube_pos.link_plate=id_plate AND link_tube=NULL "
                    + "AND NOT EXISTS (   SELECT plate_link_tube_pos.link_plate   FROM plate_link_tube_pos, tube_pos_link_task   "
                    + "WHERE tube_pos_link_task.link_tube_pos=id_link_tube_pos AND plate_link_tube_pos.link_plate=p.id_plate )";

            prepstat = conn.prepareStatement(SQLVal);

         //   prepstat.setInt(1, IdDevice);
           // prepstat.setInt(2, idUser);

            rs = prepstat.executeQuery();

            while (rs.next()) {

                res = rs.getInt(1);
                break;
            }

            rs.close();
            conn.close();

        } catch (Exception E) {
            System.out.println(E.getMessage());
            log.error("getUserEmptyRacks: " + E.getMessage());
        }

        return res;
    }

    public TreeItem<BufferModel> getBufferView(int IdDevice) {

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet rs = null;

        Connection conn;

        final TreeItem<BufferModel> rootItem = new TreeItem<>(new BufferModel(null, "1"));

        try {

            conn = getConnection();

            SQLVal = "SELECT id_cassette, cassette_id, id_level, level_id, id_plate, plate_bc, link_task, rack_conf_id FROM cassettes "
                    + "INNER JOIN levels ON level_cassette=id_cassette LEFT JOIN plates P ON levels.link_plate=id_plate LEFT JOIN plate_link_task ON plate_link_task.link_plate=id_plate "
                    + "AND link_task IN (   SELECT MAX(link_task)   FROM plate_link_task   WHERE plate_link_task.link_plate=P.id_plate ) LEFT JOIN rack_configuration ON plate_conf=id_rack_conf "
                    + "WHERE cassette_device="+IdDevice +" GROUP BY id_cassette, cassette_id, id_level, level_id, id_plate, plate_bc, link_task, rack_conf_id ORDER BY cassette_id, level_id DESC";

            prepstat = conn.prepareStatement(SQLVal);

        //    prepstat.setInt(1, IdDevice);

            rs = prepstat.executeQuery();

            int idCassette = 0;

            TreeItem<BufferModel> cassetteItem = null;
            Level level;
            Plate plate;

            String isUnknown = "";

            while (rs.next()) {

                if (idCassette != rs.getInt("id_cassette")) {

                    cassetteItem = new TreeItem<>(new BufferModel(new Cassette(rs.getInt("id_cassette"), rs.getInt("cassette_id")), ""));
                    rootItem.getChildren().add(cassetteItem);

                    idCassette = rs.getInt("id_cassette");
                }

                level = new Level(rs.getInt("id_level"), rs.getInt("level_id"), rs.getInt("cassette_id"), 0);

                if (rs.getInt("id_plate") > 0) {

                    plate = new Plate(rs.getInt("id_plate"), rs.getString("plate_bc"), "", rs.getInt("type_number"));
                    level.setPlate(plate);

                    if (rs.getInt("link_task") == 0) {
                        isUnknown = "Unknown";
                    } else {
                        isUnknown = "";
                    }

                } else {
                    isUnknown = "";
                }

                TreeItem<BufferModel> levelItem = new TreeItem<>(new BufferModel(level, isUnknown));

                cassetteItem.getChildren().add(levelItem);

            }

            rs.close();
            conn.close();

        } catch (Exception E) {
            System.out.println(E.getMessage());
            log.error("getBufferView: " + E.getMessage());
        }

        return rootItem;

    }

    public void getUserLabware(List<PartitionTubeTypes> PartitionTubeTypesList, int idUser) {

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet rs = null;

        Connection conn;

        List<String> partList = new ArrayList<>();

        try {

            conn = getConnection();

            SQLVal = "SELECT partition_id \n" +
"                    FROM user_link_partition, PARTITIONS \n" +
"                    WHERE link_partition=id_partition AND link_user=?";

            prepstat = conn.prepareStatement(SQLVal);

            prepstat.setInt(1, idUser);

            rs = prepstat.executeQuery();

            while (rs.next()) {

                partList.add(rs.getString("partition_id"));

                System.out.println("User has access to: " + rs.getString("partition_id"));
                log.info("User has access to: " + rs.getString("partition_id"));

            }

            prepstat.close();
            rs.close();

            for (String partName : partList) {

                for (PartitionTubeTypes ptt : PartitionTubeTypesList) {

                    if (partName.equals(ptt.getPartitionName())) {

                        System.out.println("Check labware Partition: " + ptt.getPartitionName());

                        for (RackTubeType rtt : ptt.getTubesTypes()) {

                            SQLVal = "SELECT id_rack_conf, rack_conf_name, rack_conf_id, id_tube_type, tube_type_name, tube_type\n" +
"FROM cassette_link_partition, partitions, cassettes,\n" +
"cassette_configuration, cassettes_types_link_racks, rack_configuration, plate_types_link_tubes, tubes_types\n" +
"WHERE partition_id=? AND link_partition=id_partition AND link_cassette=id_cassette AND\n" +
"link_cassette_type=id_cassette_conf AND link_rack_type=id_rack_conf AND link_plate_type=id_rack_conf AND link_tube_type=id_tube_type\n" +
"GROUP BY id_rack_conf, rack_conf_name, rack_conf_id, id_tube_type, tube_type_name, tube_type";

                            prepstat = conn.prepareStatement(SQLVal);

                            prepstat.setString(1, rtt.getTubeTypeName());

                            rs = prepstat.executeQuery();

                            System.out.println("Check labware: " + rtt.getTubeTypeName());

                            while (rs.next()) {

                                System.out.println("OK");

                                rtt.setRackTypeID(rs.getInt(1));
                                rtt.setRackType(rs.getInt(2));
                                rtt.setRackTypeName(rs.getString(3));

                                rtt.setTubeTypeID(rs.getInt(4));
                                rtt.setTubeType(rs.getInt(6));

                                break;

                            }

                            prepstat.close();
                            rs.close();
                        }

                    }

                }

            }

            rs.close();
            conn.close();

        } catch (Exception E) {
            System.out.println(E.getMessage());
            log.error("getUserLabware: " + E.getMessage());
        }

    }

    public String getStatus(int id) {
        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;
        String status = "";

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "select id_task, ID_JOB, TASK_STATUS from tasks inner join jobs on tasks.LINK_JOB = jobs.ID_JOB\n" +
"inner join TASK_LINK_STATUS on TASK_LINK_STATUS.LINK_TASK = tasks.ID_TASK\n" +
"inner join task_status on TASK_STATUS.ID_TASK_STATUS = TASK_LINK_STATUS.LINK_STATUS\n" +
"where id_job = ?\n" +
"order by TASK_STATUS.ID_TASK_STATUS";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, id);

            rs = stat.executeQuery();

            while (rs.next()) {
                status = rs.getString("TASK_STATUS");

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: gettaskId:  " + E.getMessage());
            log.error("ERROR: gettaskId:  " + E.getMessage());

        }
        return status;
    }

    public int getTaskId(int jobid) {
        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;
        int taskId = 0;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "select id_task, ID_JOB from tasks inner join jobs on tasks.LINK_JOB = jobs.ID_JOB where id_job = ?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, jobid);

            rs = stat.executeQuery();

            while (rs.next()) {
                taskId = rs.getInt("ID_TASK");

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: gettaskId:  " + E.getMessage());
            log.error("ERROR: gettaskId:  " + E.getMessage());

        }
        return taskId;
    }

    public synchronized int IsImportRackExists(String Barcode) {

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            // Find in loaded
            SQLVal = "SELECT ID_PLATE \n" +
"                FROM plates, levels\n" +
"                WHERE plate_bc=? AND link_plate=id_plate";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, Barcode);

            rs = stat.executeQuery();

            while (rs.next()) {

                rs.close();
                stat.close();
                conn.close();

                log.info("Rack: " + Barcode + " already exists");

                return 1;

            }

            rs.close();
            stat.close();

            // Find in Preloade
            SQLVal = "SELECT id_plate "
                    + "FROM plates P "
                    + "WHERE plate_bcr=? AND plate_preloaded=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, Barcode);
            stat.setString(2, "1");

            rs = stat.executeQuery();

            while (rs.next()) {

                rs.close();
                stat.close();
                conn.close();

                log.info("Rack: " + Barcode + " already preloaded");

                return 2;

            }

            rs.close();
            stat.close();
            conn.close();

            return 0;

        } catch (Exception E) {
            System.out.println("ERROR: Check Import Rack: " + E.getMessage());
            log.error("IsImportRackExists: " + E.getMessage());
            return -1;
        }

    }

    public void CreateImportJob(String Barcode, int RackType, int TubeType, boolean isEmpty, String TargetPartition, int UserId) {

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        int idPlate = 0;
        int IdJobType = 0;
        int IdJob = 0;

        String SQLVal;

        try {

            log.info("CreateImportJob: Barcode=" + Barcode + ", RackType=" + RackType + ", TubeType=" + TubeType + ", Empty=" + isEmpty + ", TargetPartition=" + TargetPartition + ", UserId=" + UserId);

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            // Inset Plate
            SQLVal = "INSERT INTO plates (plate_bc, plate_conf, plate_preloaded) VALUES (?, ?, ?)";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, Barcode);
            stat.setInt(2, RackType);
            stat.setInt(3, 1);

            stat.executeUpdate();
            stat.close();

            SQLVal = "SELECT id_plate FROM plates WHERE plate_bc=? AND plate_conf=? AND plate_preloaded=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, Barcode);
            stat.setInt(2, RackType);
            stat.setInt(3, 1);

            rs = stat.executeQuery();

            while (rs.next()) {

                idPlate = rs.getInt("id_plate");
                break;
            }

            rs.close();
            stat.close();

            // CREATE JOB
            // Select Job Type
            SQLVal = "SELECT id_job_type "
                    + "FROM job_types "
                    + "WHERE job_type=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, "Import");

            rs = stat.executeQuery();

            while (rs.next()) {
                IdJobType = rs.getInt("id_job_type");
            }

            rs.close();
            stat.close();

            // Insert Job
            SQLVal = "INSERT INTO jobs (job_type, link_user) VALUES (?, ?)";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, IdJobType);
            stat.setInt(2, UserId);

            stat.executeUpdate();

            stat.close();

            // Select Job
            SQLVal = "SELECT MAX(id_job) FROM jobs WHERE job_type=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, IdJobType);

            rs = stat.executeQuery();

            while (rs.next()) {
                IdJob = rs.getInt(1);
                break;
            }

            rs.close();
            stat.close();

            // Job Property
            if (isEmpty) {
                setJobProperty(IdJob, "Empty Rack", null);
            }

            setJobProperty(IdJob, "Target Partition", TargetPartition);
            setJobProperty(IdJob, "Buffer delay", null);

            int idTask = 0;

            // Create tasks            
            if (!isEmpty) {

                idTask = AddNewTask("Import Plate", "Created", IdJob, UserId, 1);

                AddPlateToTask(idPlate, idTask);

                idTask = AddNewTask("Scan 2D", "Created", IdJob, UserId, 2);

                setTaskProperty(idTask, "Date to run", null);
                setTaskProperty(idTask, "Target Partition", TargetPartition);

                AddPlateToTask(idPlate, idTask);

            } else {

                // Set Tubes Positions
                SQLVal = "INSERT INTO plate_link_tube_pos (link_plate, link_tube_pos) "
                        + "SELECT '" + String.valueOf(idPlate) + "', id_tube_position "
                        + "FROM tube_positions "
                        + "WHERE tube_pos_type=?";

                stat = conn.prepareStatement(SQLVal);

                stat.setInt(1, TubeType);

                stat.executeUpdate();
                stat.close();

                idTask = AddNewTask("Import Plate", "Created", IdJob, UserId, 1);

                AddPlateToTask(idPlate, idTask);
            }

            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: Create Import Job: " + E.getMessage());
            log.error("CreateImportJob: " + E.getMessage());
        }
    }

    private void setJobProperty(int idJob, String JobProperty, String JobPropertyValue) {

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        int IdJobProperty = 0;

        String SQLVal;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            // Selecet Job Property
            SQLVal = "SELECT id_job_property "
                    + "FROM job_properties "
                    + "WHERE job_property=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, JobProperty);

            rs = stat.executeQuery();

            while (rs.next()) {
                IdJobProperty = rs.getInt("id_job_property");
                break;
            }

            rs.close();
            stat.close();

            if (IdJobProperty == 0) {

                SQLVal = "INSERT INTO job_properties (job_property) VALUES (?)";

                stat = conn.prepareStatement(SQLVal);

                stat.setString(1, JobProperty);

                stat.executeUpdate();

                stat.close();

                SQLVal = "SELECT id_job_property "
                        + "FROM job_properties "
                        + "WHERE job_property=?";

                stat = conn.prepareStatement(SQLVal);

                stat.setString(1, JobProperty);

                rs = stat.executeQuery();

                while (rs.next()) {
                    IdJobProperty = rs.getInt("id_job_property");
                    break;
                }

                rs.close();
                stat.close();

            }

            // Insert JOB LINK PROPERTY
            SQLVal = "INSERT INTO job_link_property (link_job, link_property) VALUES (?, ?)";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, idJob);
            stat.setInt(2, IdJobProperty);

            stat.executeUpdate();

            stat.close();

       if (JobPropertyValue != null)
       {
           SQLVal = "INSERT INTO job_link_property (link_job, link_property, job_property_value) VALUES (?, ?, ?)";
           
           stat = conn.prepareStatement(SQLVal);
           
           stat.setInt(1, idJob);
           stat.setInt(2, IdJobProperty);
           stat.setString(3, JobPropertyValue);
           
           stat.executeUpdate();
           
           stat.close();
       }
       else
       {
           SQLVal = "INSERT INTO job_link_property (link_job, link_property) VALUES (?, ?)";
           
           stat = conn.prepareStatement(SQLVal);
           
           stat.setInt(1, idJob);
           stat.setInt(2, IdJobProperty);
           
           stat.executeUpdate();
           
        stat.close();
      }

            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: Add Job Property: " + E.getMessage());
            log.error("setJobProperty: " + E.getMessage());
        }

    }

    public int AddNewTask(String TaskType, String TaskStatus, int JobId, int UserId, int TaskNumber) {
        int res = 0;

        int IdTaskType = 0;
        int idTaskStatus = 0;

        Connection conn;
        PreparedStatement stat;
        String SQLVal;
        ResultSet rs;

        try {
            log.info("AddNewTask:  TaskType=" + TaskType + ", TaskStatus=" + TaskStatus + ", JobId=" + JobId + ", UserId=" + UserId + ", TaskNumber=" + TaskNumber);

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_task_type FROM task_types WHERE task_type=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, TaskType);

            rs = stat.executeQuery();

            while (rs.next()) {
                IdTaskType = rs.getInt("id_task_type");
                break;
            }

            rs.close();
            stat.close();

            SQLVal = "SELECT id_task_status FROM task_status WHERE task_status=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, TaskStatus);

            rs = stat.executeQuery();

            while (rs.next()) {
                idTaskStatus = rs.getInt("id_task_status");
                break;
            }

            rs.close();
            stat.close();

            SQLVal = "INSERT INTO tasks (task_user, task_time, task_type, task_sequence, link_job) VALUES "
                    + "(?, current_timestamp, ?, ?, ?)";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, UserId);
            stat.setInt(2, IdTaskType);
            stat.setInt(3, TaskNumber);
            stat.setInt(4, JobId);

            stat.executeUpdate();

            stat.close();

            SQLVal = "SELECT MAX(id_task) FROM tasks WHERE task_user=? AND task_type=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, UserId);
            stat.setInt(2, IdTaskType);

            rs = stat.executeQuery();

            while (rs.next()) {
                res = rs.getInt(1);
                break;
            }

            rs.close();
            stat.close();

            SQLVal = "INSERT INTO task_link_status "
                    + "(link_task, link_status, status_user, status_time) "
                    + "VALUES "
                    + "(?,?,?,current_timestamp)";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, res);
            stat.setInt(2, idTaskStatus);
            stat.setInt(3, UserId);

            stat.executeUpdate();

            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: AddNewTask: " + E.getMessage());
            log.error("AddNewTask: " + E.getMessage());
        }

        return res;

    }

    private void setTaskProperty(int idTask, String TaskProperty, String TaskPropertyValue) {

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        int IdTaskProperty = 0;

        String SQLVal;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            // Selecet Task Property
            SQLVal = "SELECT id_task_property "
                    + "FROM task_properties "
                    + "WHERE task_property=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setString(1, TaskProperty);

            rs = stat.executeQuery();

            while (rs.next()) {
                IdTaskProperty = rs.getInt("id_task_property");
                break;
            }

            rs.close();
            stat.close();

            if (IdTaskProperty == 0) {

                SQLVal = "INSERT INTO task_properties (task_property) VALUES (?)";

                stat = conn.prepareStatement(SQLVal);

                stat.setString(1, TaskProperty);

                stat.executeUpdate();

                stat.close();

                SQLVal = "SELECT id_task_property "
                        + "FROM task_properties "
                        + "WHERE task_property=?";

                stat = conn.prepareStatement(SQLVal);

                stat.setString(1, TaskProperty);

                rs = stat.executeQuery();

                while (rs.next()) {
                    IdTaskProperty = rs.getInt("id_task_property");
                    break;
                }

                rs.close();
                stat.close();

            }

            // Insert TASK LINK PROPERTY
            SQLVal = "INSERT INTO task_link_property (link_task, link_property) VALUES (?, ?)";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, idTask);
            stat.setInt(2, IdTaskProperty);

            stat.executeUpdate();

            stat.close();

            if (TaskPropertyValue == null) {
                
            } else {
                int idTaskLinkProperty = 0;

                SQLVal = "SELECT id_task_link_property "
                        + "FROM task_link_property "
                        + "WHERE link_task=? AND link_property=?";

                stat = conn.prepareStatement(SQLVal);

                stat.setInt(1, idTask);
                stat.setInt(2, IdTaskProperty);

                rs = stat.executeQuery();

                while (rs.next()) {
                    idTaskLinkProperty = rs.getInt("id_task_link_property");
                    break;
                }

                rs.close();
                stat.close();

                SQLVal = "INSERT INTO task_link_property (link_task, link_property, task_property_value) VALUES (?, ?, ?)";
        
        stat = conn.prepareStatement(SQLVal);
        
        stat.setInt(1, idTask);
        stat.setInt(2, IdTaskProperty);
        stat.setString(3, TaskPropertyValue);
        stat.executeUpdate();

                stat.close();
            }

            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: Add Task Property: " + E.getMessage());
            log.error("setTaskProperty: " + E.getMessage());
        }

    }

    private void AddPlateToTask(int IdPlate, int IdTask) {

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        int id_src_link_task = 0;

        String SQLVal;

        try {
            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "INSERT INTO task_src_link_trg (link_task, is_done) VALUES (?,1)";

            stat = conn.prepareStatement(SQLVal);
            stat.setInt(1, IdTask);

            stat.executeUpdate();

            stat.close();

            SQLVal = "SELECT MAX(id_src_link_trg) "
                    + "FROM task_src_link_trg "
                    + "WHERE link_task=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, IdTask);

            rs = stat.executeQuery();

            while (rs.next()) {
                id_src_link_task = rs.getInt(1);
                break;
            }

            rs.close();
            stat.close();

            SQLVal = "INSERT INTO plate_link_task (link_task, link_plate) VALUES (?,?)";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, id_src_link_task);
            stat.setInt(2, IdPlate);

            stat.executeUpdate();

            stat.close();

        } catch (Exception E) {
            System.out.println("ERROR: Add Palte to Task: " + E.getMessage());
            log.error("AddPlateToTask: " + E.getMessage());
        }

    }

    public TreeItem<ImportTaskTableModel> getImportTaskList(int idUser) {

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        int id_Job = 0;

        String SQLVal;

        int Jobcount = 0;

        String taskCaption;

        final TreeItem<ImportTaskTableModel> rootItem = new TreeItem<>(new ImportTaskTableModel(null, 0, "", "", "", Jobcount));

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_job, id_task, task_types.task_type, task_status, id_plate, plate_bc, task_sequence, note FROM jobs, job_types, tasks T, "
                    + "task_types, task_link_status, task_status, task_src_link_trg, plate_link_task,  plates WHERE done=0 AND jobs.job_type=job_types.id_job_type AND job_types.job_type='Import' "
                    + "AND link_job=id_job AND T.task_type=id_task_type AND task_link_status.link_task=id_task AND task_link_status.link_status=id_task_status AND task_src_link_trg.link_task=id_task AND "
                    + "plate_link_task.link_task=id_src_link_trg AND link_plate=id_plate AND id_task_link_status= (     SELECT MAX(id_task_link_status)     FROM task_link_status     WHERE link_task=T.id_task )"
                    + " AND jobs.link_user IN(   SELECT id_user   FROM users, user_link_partition   WHERE link_user=id_user AND link_partition IN"
                    + " (     SELECT id_partition     FROM users, user_link_partition, partitions, cassette_link_partition, cassettes, devices,    "
                    + " device_params, param_link_device     WHERE param_name='Storage' AND id_param=link_param AND link_device=id_device AND    "
                    + " cassette_device=id_device AND cassette_link_partition.link_cassette=id_cassette AND     "
                    + "cassette_link_partition.link_partition=id_partition AND     user_link_partition.link_partition=id_partition AND link_user=id_user "
                    + "AND id_user="+idUser+" "
                    + "GROUP BY id_partition, partition_id   ) ) GROUP BY id_job, id_task, task_types.task_type, task_status, id_plate, plate_bc, task_sequence, note ORDER BY id_job, task_sequence";

            stat = conn.prepareStatement(SQLVal);
            
       //     stat.setString(2, "Storage");

            rs = stat.executeQuery();

            TreeItem<ImportTaskTableModel> jobItem = null;

            while (rs.next()) {

                if (id_Job != rs.getInt("id_job")) {

                    Jobcount++;

                    id_Job = rs.getInt("id_job");

                    Plate plate = new Plate(rs.getInt("id_plate"), rs.getString("plate_bc"), "", 0);

                    jobItem = new TreeItem<>(new ImportTaskTableModel(plate, id_Job, "", "", "", Jobcount));

                    jobItem.setExpanded(true);

                    rootItem.getChildren().add(jobItem);

                }

                if (rs.getString(3).equals("Import Plate")) {

                    taskCaption = "Verify Plate";

                } else if (rs.getString(3).equals("Scan 2D")) {

                    taskCaption = "Scan and Store Plate";

                } else if (rs.getString(3).equals("Move Plate")) {

                    taskCaption = "Store Plate";
                } else {
                    taskCaption = rs.getString(3);
                }

                TreeItem<ImportTaskTableModel> taskItem = new TreeItem<>(new ImportTaskTableModel(null, rs.getInt("id_task"), taskCaption, rs.getString("task_status"), rs.getString("note"), Jobcount));

                jobItem.getChildren().add(taskItem);

            }

            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: getImportTaskList: " + E.getMessage());
            log.error("getImportTaskList: " + E.getMessage());
        }

        return rootItem;
    }

    public TreeItem<ExportTaskTableModel> getExportTaskList(int idUser) {

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        int id_Job = 0;
        String jobId = "";

        String SQLVal;

        int Jobcount = 0;

        String taskCaption;

        final TreeItem<ExportTaskTableModel> rootItem = new TreeItem<>(new ExportTaskTableModel(0, "", 0, "", "", "", Jobcount));

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_job, job_id, id_task, task_types.task_type, task_status, task_sequence, note FROM jobs, job_types, tasks T, task_types, task_link_status, task_status "
                    + "WHERE done=0 AND jobs.job_type=job_types.id_job_type AND (job_types.job_type='Job File' OR job_types.job_type='Export') AND link_job=id_job AND T.task_type=id_task_type "
                    + "AND task_link_status.link_task=id_task AND task_link_status.link_status=id_task_status AND id_task_link_status= (     SELECT MAX(id_task_link_status)     "
                    + "FROM task_link_status     WHERE link_task=T.id_task ) AND jobs.link_user IN(   SELECT id_user   FROM users, user_link_partition   "
                    + "WHERE link_user=id_user AND link_partition IN (     SELECT id_partition     "
                    + "FROM users, user_link_partition, partitions, cassette_link_partition, cassettes, devices,     device_params, param_link_device     WHERE param_name='Storage' "
                    + "AND id_param=link_param AND link_device=id_device AND cassette_device=id_device AND cassette_link_partition.link_cassette=id_cassette "
                    + "AND cassette_link_partition.link_partition=id_partition AND"
                    + "  user_link_partition.link_partition=id_partition AND link_user=id_user AND id_user="+idUser+" GROUP BY id_partition, partition_id   ) ) "
                    + "GROUP BY id_job, job_id, id_task, task_types.task_type, task_status, task_sequence, note ORDER BY id_job, task_sequence";

            stat = conn.prepareStatement(SQLVal);
//            stat.setString(1, "Job File");
//            stat.setString(2, "Export");
//            stat.setString(3, "Storage");
     //       stat.setInt(4, idUser);

            rs = stat.executeQuery();

            TreeItem<ExportTaskTableModel> jobItem = null;

            while (rs.next()) {

                if (id_Job != rs.getInt("id_job")) {

                    Jobcount++;

                    id_Job = rs.getInt("id_job");

                    if (rs.getString("job_id") != null) {
                        jobId = rs.getString("job_id");
                    } else {
                        jobId = "";
                    }

                    jobItem = new TreeItem<>(new ExportTaskTableModel(id_Job, jobId, 0, "", "", "", Jobcount));

                    jobItem.setExpanded(true);

                    rootItem.getChildren().add(jobItem);

                }

                if (rs.getString(4).equals("Job File")) {

                    taskCaption = "Pick Tubes";

                } else {
                    taskCaption = rs.getString(4);
                }

                TreeItem<ExportTaskTableModel> taskItem = new TreeItem<>(new ExportTaskTableModel(0, "", rs.getInt("id_task"), taskCaption, rs.getString("task_status"), rs.getString("note"), Jobcount));

                jobItem.getChildren().add(taskItem);

            }

            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR: getExportTaskList: " + E.getMessage());
            log.error("getExportTaskList: " + E.getMessage());
        }

        return rootItem;
    }

    public List<TaskHistoryModel> getTaskHistory(int idTask) {

        List<TaskHistoryModel> taskHistoryModels = new ArrayList<>();

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        TaskHistoryModel taskHistoryModel = null;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_task_link_status, task_status, status_time, user_login, note "
                    + "FROM task_link_status, task_status, users "
                    + "WHERE link_task=? AND link_status=id_task_status AND id_user=status_user "
                    + "ORDER BY id_task_link_status";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, idTask);

            rs = stat.executeQuery();

            while (rs.next()) {

                taskHistoryModel = new TaskHistoryModel(rs.getString("task_status"), df.format(new Date(rs.getTimestamp("status_time").getTime())), rs.getString("user_login"), rs.getString("note"));

                taskHistoryModels.add(taskHistoryModel);

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR get task history: " + E.getMessage());
            log.error("getTaskHistory: " + E.getMessage());
        }

        return taskHistoryModels;
    }

    public ObservableList<ExportExceptioRacksModel> getExceptionRacks() {

        ObservableList<ExportExceptioRacksModel> historyTaskTableData = FXCollections.observableArrayList();

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_plate, plate_bc, cassette_id, level_id FROM cassettes, levels, plates WHERE cassette_is_safe_area=1 "
                    + "AND level_cassette=id_cassette AND link_plate=id_plate ORDER BY cassette_id, level_id";

            stat = conn.prepareStatement(SQLVal);

            rs = stat.executeQuery();

            while (rs.next()) {

                historyTaskTableData.add(new ExportExceptioRacksModel(rs.getInt("id_plate"), rs.getString("plate_bc"), rs.getInt("cassette_id"), rs.getInt("level_id")));
            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR getExceptionRacks: " + E.getMessage());
            log.error("getExceptionRacks: " + E.getMessage());
        }

        return historyTaskTableData;
    }

    public List<TaskPropertyModel> getTaskProperty(int idTask) {

        List<TaskPropertyModel> taskPropertyModels = new ArrayList<>();

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        TaskPropertyModel taskPropertyModel = null;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT task_property, task_property_value FROM task_link_property, task_properties WHERE link_task=? AND link_property=id_task_property";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, idTask);

            rs = stat.executeQuery();

            while (rs.next()) {

                String value = "";

                if (rs.getString("task_property_date") != null) {

                    value = df.format(new Date(rs.getTimestamp("task_property_date").getTime()));

                } else if (rs.getString("task_property_value") != null) {
                    value = rs.getString("task_property_value");
                }

                taskPropertyModel = new TaskPropertyModel(rs.getString("task_property"), value);

                taskPropertyModels.add(taskPropertyModel);

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR get task property: " + E.getMessage());
            log.error("getTaskProperty: " + E.getMessage());
        }

        return taskPropertyModels;
    }

    public List<ExportTubePickModel> getPickJobDetails(int id) {

        List<ExportTubePickModel> list = new ArrayList<>();

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        ExportTubePickModel exportTubePickModel = null;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_src_link_trg, is_done, src_trg_info, src_tube_bcr "
                    + "FROM task_src_link_trg, tasks, src_trg_link_csv, task_csv_sequence "
                    + "WHERE link_task=id_task AND id_task=? AND link_src_trg=id_src_link_trg "
                    + "AND link_csv=id_csv_sequence";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, id);

            rs = stat.executeQuery();

            String is_done = "";

            String value = "";

            while (rs.next()) {

                if (rs.getInt("is_done") == 0) {
                    is_done = "";
                } else {
                    is_done = "Done";
                }

                if (rs.getString("src_tube_bcr") == null) {
                    value = "";
                } else {
                    value = rs.getString("src_tube_bcr");
                }

                exportTubePickModel = new ExportTubePickModel(rs.getInt("id_src_link_trg"), value, 0, 0, "", "", 0, 0, is_done, rs.getString("src_trg_info"));

                list.add(exportTubePickModel);

            }

            rs.close();
            stat.close();

            for (ExportTubePickModel etpm : list) {

                if (!etpm.getStatus().equals("Done")) {
                    // SRC
                    SQLVal = "SELECT tube_bc, tube_x, tube_y, plate_bc FROM tube_link_task INNER JOIN tubes ON tube_link_task.link_tube=id_tube AND tube_link_task.link_task=? "
                            + "INNER JOIN plate_link_tube_pos ON plate_link_tube_pos.link_tube=id_tube INNER JOIN tube_positions "
                            + "ON link_tube_pos=id_tube_position INNER JOIN plates ON link_plate=id_plate";
                } else {
                    SQLVal = "SELECT t.TUBE_BC, tp.TUBE_X, tp.TUBE_Y, p.PLATE_BC\n"
                            + "FROM TUBE_LINK_TASK tlt \n"
                            + "	INNER JOIN TUBES t ON ( tlt.LINK_TUBE = t.ID_TUBE  )  \n"
                            + "		INNER JOIN HISTORY_LINK_TUBE hlt ON ( t.ID_TUBE = hlt.LINK_TUBE  )  \n"
                            + "			INNER JOIN HISTORY h ON ( hlt.LINK_HISTORY = h.ID_HISTORY  )  \n"
                            + "				INNER JOIN HISTORY_LINK_TUBE_POS hltp ON ( h.ID_HISTORY = hltp.LINK_HISTORY  )  \n"
                            + "					INNER JOIN PLATE_LINK_TUBE_POS pltp ON ( hltp.LINK_TUBE_POS = pltp.ID_LINK_TUBE_POS  )  \n"
                            + "						INNER JOIN PLATES p ON ( pltp.LINK_PLATE = p.ID_PLATE  )  \n"
                            + "						INNER JOIN TUBE_POSITIONS tp ON ( pltp.LINK_TUBE_POS = tp.ID_TUBE_POSITION  )  \n"
                            + "WHERE tlt.LINK_TASK =? "
                            + "ORDER BY h.ID_HISTORY DESC";
                }
                stat = conn.prepareStatement(SQLVal);

               stat.setInt(1, etpm.getId());

                rs = stat.executeQuery();

                while (rs.next()) {

                    if (rs.getString("plate_bc") == null) {
                        value = "";
                    } else {
                        value = rs.getString("plate_bc");
                    }

                    etpm.setSrcRackBC(value);

                    etpm.setSrcX(rs.getInt("tube_x"));

                    etpm.setSrcY(rs.getInt("tube_y"));

                    if (rs.getString("tube_bc") != null) {

                        if (!etpm.getSrcTubeBC().equals(rs.getString("tube_bc"))) {
                            value = etpm.getSrcTubeBC();
                            etpm.setSrcTubeBC(value + "/" + rs.getString("tube_bc"));
                        }
                    }
                }

                rs.close();
                stat.close();

                // TRG
                SQLVal = "SELECT tube_x, tube_y, plate_bc "
                        + "FROM tube_pos_link_task "
                        + "INNER JOIN plate_link_tube_pos ON tube_pos_link_task.link_tube_pos=id_link_tube_pos "
                        + "AND tube_pos_link_task.link_task=? "
                        + "INNER JOIN tube_positions ON plate_link_tube_pos .link_tube_pos=id_tube_position "
                        + "INNER JOIN plates ON link_plate=id_plate";

                stat = conn.prepareStatement(SQLVal);

                stat.setInt(1, etpm.getId());

                rs = stat.executeQuery();

                while (rs.next()) {

                    if (rs.getString("plate_bc") == null) {
                        value = "";
                    } else {
                        value = rs.getString("plate_bc");
                    }

                    etpm.setTrgRackBC(value);

                    etpm.setTrgX(rs.getInt("tube_x"));

                    etpm.setTrgY(rs.getInt("tube_y"));

                }

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR get task property: " + E.getMessage());
            log.error("getTaskProperty: " + E.getMessage());
        }

        return list;

    }

    /*
    public List<ExportTubePickModel> getPickJobDetails(int id) {

        List<ExportTubePickModel> list = new ArrayList<>();

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        ExportTubePickModel exportTubePickModel = null;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_src_link_trg, is_done, src_trg_info, src_tube_bcr "
                    + "FROM task_src_link_trg, tasks, src_trg_link_csv, task_csv_sequence "
                    + "WHERE link_task=id_task AND id_task=? AND link_src_trg=id_src_link_trg "
                    + "AND link_csv=id_csv_sequence";

            stat = conn.prepareStatement(SQLVal);
            System.out.println("id1"+id);

            stat.setInt(1, id);

            rs = stat.executeQuery();

            String is_done = "";

            String value = "";

            while (rs.next()) {

                if (rs.getInt("is_done") == 0) {
                    is_done = "";
                } else {
                    is_done = "Done";
                }

                if (rs.getString("src_tube_bcr") == null) {
                    value = "";
                } else {
                    value = rs.getString("src_tube_bcr");
                }

                exportTubePickModel = new ExportTubePickModel(rs.getInt("id_src_link_trg"), value, 0, 0, "", "", 0, 0, is_done, rs.getString("src_trg_info"));

                list.add(exportTubePickModel);

            }

            rs.close();
            stat.close();

            for (ExportTubePickModel etpm : list) {

                // SRC
                SQLVal = "SELECT tube_bcr, tube_x, tube_y, plate_bcr "
                        + "FROM tube_link_task "
                        + "INNER JOIN tubes ON tube_link_task.link_tube=id_tube AND tube_link_task.link_task=? "
                        + "INNER JOIN tubes_link_plate ON tubes_link_plate.link_tube=id_tube "
                        + "INNER JOIN plate_link_tube_pos ON link_pos=id_link_tube_pos "
                        + "INNER JOIN tube_positions ON link_tube_pos=id_tube_position "
                        + "INNER JOIN plates ON link_plate=id_plate";

                stat = conn.prepareStatement(SQLVal);

                System.out.println("id2" + etpm.getId());

                stat.setInt(1, etpm.getId());

                rs = stat.executeQuery();

                while (rs.next()) {

                    if (rs.getString("plate_bcr") == null) {
                        value = "";
                    } else {
                        value = rs.getString("plate_bcr");
                    }

                    etpm.setSrcRackBC(etpm.getSrcRackBC());

                    etpm.setSrcX(rs.getInt("tube_x"));

                    etpm.setSrcY(rs.getInt("tube_y"));

                    if (rs.getString("tube_bcr") != null) {

                        if (!etpm.getSrcTubeBC().equals(rs.getString("tube_bcr"))) {
                            value = etpm.getSrcTubeBC();
                            
                            etpm.setSrcRackBC(value + "/" + rs.getString("tube_bcr"));
                        }
                    }
                }

                rs.close();
                stat.close();

                // TRG
                SQLVal = "SELECT tube_x, tube_y, plate_bcr "
                        + "FROM tube_pos_link_task "
                        + "INNER JOIN plate_link_tube_pos ON tube_pos_link_task.link_tube_pos=id_link_tube_pos AND tube_pos_link_task.link_task=? "
                        + "INNER JOIN tube_positions ON plate_link_tube_pos .link_tube_pos=id_tube_position "
                        + "INNER JOIN plates ON link_plate=id_plate";

                stat = conn.prepareStatement(SQLVal);

                stat.setInt(1, etpm.getId());

                rs = stat.executeQuery();

                String value1 = "";
                while (rs.next()) {

                    if (rs.getString("plate_bcr") == null) {
                        value1 = "";
                    } else {
                        value1 = rs.getString("plate_bcr");
                    }

                    etpm.setTrgRackBC(value1);

                    etpm.setTrgX(rs.getInt("tube_x"));

                    etpm.setTrgY(rs.getInt("tube_y"));

                }

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR get task property: " + E.getMessage());
            log.error("getTaskProperty: " + E.getMessage());
        }

        return list;

    }
     */
    public List<ExportPlateModel> getExportPlateDetails(int id) {

        List<ExportPlateModel> list = new ArrayList<>();

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        ExportPlateModel exportPlateModel = null;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT device_id, partition_id, cassette_id, level_id, id_plate, plate_bc FROM devices, cassettes, cassette_link_partition, partitions, levels, plates, plate_link_task, "
                    + "task_src_link_trg WHERE id_device=cassette_device AND link_cassette=id_cassette AND link_partition=id_partition "
                    + "AND level_cassette=id_cassette AND levels.link_plate=id_plate AND plate_link_task.link_plate=id_plate "
                    + "AND plate_link_task.link_task=id_src_link_trg AND task_src_link_trg.link_task=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, id);

            rs = stat.executeQuery();

            String PlateBC = "";
            String device = "";
            String partition = "";

            while (rs.next()) {

                if (rs.getString("plate_bc") == null) {
                    PlateBC = "";
                } else {
                    PlateBC = rs.getString("plate_bc");
                }

                if (rs.getString("device_id") == null) {
                    device = "";
                } else {
                    device = rs.getString("device_id");
                }

                if (rs.getString("partition_id") == null) {
                    partition = "";
                } else {
                    partition = rs.getString("partition_id");
                }

                exportPlateModel = new ExportPlateModel(id, rs.getInt("id_plate"), PlateBC, device, partition, rs.getInt("cassette_id"), rs.getInt("level_id"));

                list.add(exportPlateModel);

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR get task property: " + E.getMessage());
            log.error("getTaskProperty: " + E.getMessage());
        }

        return list;

    }

    public List<TaskPropertyModel> getJobProperty(int idJob, int idPlate) {

        List<TaskPropertyModel> taskPropertyModels = new ArrayList<>();

        PreparedStatement stat;
        ResultSet rs;
        Connection conn = null;

        String SQLVal;

        TaskPropertyModel taskPropertyModel = null;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            // Rack
            SQLVal = "SELECT plate_bc, tube_type_name FROM plates, rack_configuration, plate_types_link_tubes, tubes_types "
                    + "WHERE id_plate=? AND plate_conf=id_rack_conf AND link_plate_type=id_rack_conf AND link_tube_type=id_tube_type";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, idPlate);

            rs = stat.executeQuery();

            while (rs.next()) {

                taskPropertyModel = new TaskPropertyModel("Barcode", rs.getString("plate_bc"));
                taskPropertyModels.add(taskPropertyModel);

                taskPropertyModel = new TaskPropertyModel("Type", rs.getString("tube_type_name"));

                taskPropertyModels.add(taskPropertyModel);

            }

            rs.close();
            stat.close();

            // Job 
            SQLVal = "SELECT job_property, job_property_value "
                    + "FROM job_link_property "
                    + "INNER JOIN job_properties ON link_property=id_job_property "
                    + "WHERE link_job=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, idJob);

            rs = stat.executeQuery();

            while (rs.next()) {

                String value = "True";

                if (rs.getString("job_property_value") != null) {
                    value = rs.getString("job_property_value");
                }

                taskPropertyModel = new TaskPropertyModel(rs.getString("job_property"), value);

                taskPropertyModels.add(taskPropertyModel);

            }

            rs.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("ERROR get task property: " + E.getMessage());
            log.error("getJobProperty: " + E.getMessage());
        }

        return taskPropertyModels;
    }

    public synchronized boolean getWarupDelay() {

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet resultset = null;

        Connection connection;

        boolean res = false;

        try {

            connection = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_task, task_property_value FROM jobs INNER JOIN tasks T ON link_job=id_job INNER JOIN task_link_status ON task_link_status.link_task=id_task "
                    + "AND id_task_link_status IN (   SELECT MAX(id_task_link_status)   FROM task_link_status   WHERE link_task=T.id_task ) "
                    + "INNER JOIN task_status ON link_status=id_task_status AND task_status.task_status=?INNER JOIN task_link_property ON task_link_property.link_task=id_task "
                    + "INNER JOIN task_properties ON link_property=id_task_property AND task_property=? WHERE done=0 ORDER BY task_property_value";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.setString(1, "Pending");
            prepstat.setString(2, "Date to run");
            prepstat.setTimestamp(3, new Timestamp(new Date().getTime()));

            resultset = prepstat.executeQuery();

            while (resultset.next()) {

                res = true;
                break;
            }

            resultset.close();
            prepstat.close();
            connection.close();

        } catch (SQLException E) {

            log.error("getWarupDelay: " + E.getMessage());

        }

        return res;
    }

    public boolean DeleteImportJob(int IdJob) {

        String SQLVal = "";

        PreparedStatement stat = null;

        ResultSet resultset = null;

        Connection conn;

        boolean isExist;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT task_status "
                    + "FROM tasks T, task_link_status, task_status "
                    + "WHERE link_job="+IdJob+" AND link_task=id_task AND link_status=id_task_status "
                    + "AND id_task_link_status IN "
                    + "( "
                    + "    SELECT max(id_task_link_status) "
                    + "    FROM task_link_status "
                    + "    WHERE link_task=T.id_task "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

           // stat.setInt(1, IdJob);

            resultset = stat.executeQuery();

            while (resultset.next()) {

                if (resultset.getString("task_status").equals("Running")) {

                    resultset.close();
                    stat.close();
                    conn.close();

                    return false;
                }

            }

            // task_link_status
            SQLVal = "DELETE FROM task_link_status "
                    + "WHERE link_task IN "
                    + "( "
                    + "  SELECT id_task FROM tasks "
                    + "  WHERE link_job="+IdJob+" "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

         //  stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // task_link_property
            SQLVal = "DELETE FROM task_link_property "
                    + "WHERE link_task IN "
                    + "( "
                    + "  SELECT id_task FROM tasks "
                    + "  WHERE link_job="+IdJob+" "
                    + ")";
            stat = conn.prepareStatement(SQLVal);

         //   stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // tube_link_task
            SQLVal = "DELETE FROM tube_link_task "
                    + "WHERE link_task IN ( "
                    + "    SELECT id_src_link_trg "
                    + "    FROM task_src_link_trg "
                    + "    WHERE link_task IN "
                    + "    ( "
                    + "        SELECT id_task FROM tasks "
                    + "        WHERE link_job="+IdJob+" "
                    + "    ) "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

      //      stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // tube_pos_link_task
            SQLVal = "DELETE FROM tube_pos_link_task "
                    + "WHERE link_task IN ( "
                    + "    SELECT id_src_link_trg "
                    + "    FROM task_src_link_trg "
                    + "    WHERE link_task IN "
                    + "    ( "
                    + "        SELECT id_task FROM tasks "
                    + "        WHERE link_job="+IdJob+" "
                    + "    ) "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

       //     stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            isExist = false;

            SQLVal = "SELECT id_level \n" +
"FROM tasks, task_src_link_trg, plate_link_task, plates, levels\n" +
"WHERE link_job="+IdJob+" AND task_src_link_trg.link_task=id_task AND \n" +
"                    plate_link_task.link_task=id_src_link_trg AND plate_link_task.link_plate=id_plate AND \n" +
"                    levels.link_plate=id_plate";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, IdJob);

            resultset = stat.executeQuery();

            while (resultset.next()) {
                isExist = true;
                break;
            }

            resultset.close();
            stat.close();

            if (!isExist) {

                SQLVal = "SELECT PLATE_BC\n" +
"               FROM tasks, task_src_link_trg, plate_link_task, plates \n" +
"               WHERE link_job="+IdJob+" AND task_src_link_trg.link_task=id_task AND \n" +
"               plate_link_task.link_task=id_src_link_trg AND plate_link_task.link_plate=id_plate";

                stat = conn.prepareStatement(SQLVal);

                stat.setInt(1, IdJob);

                resultset = stat.executeQuery();

                while (resultset.next()) {
                    isExist = true;
                    break;
                }

                resultset.close();
                stat.close();

            }

            if (!isExist) {
                // delete Plate

                SQLVal = "DELETE FROM plates "
                        + "WHERE id_plate IN "
                        + "( "
                        + "    SELECT id_plate "
                        + "    FROM tasks, task_src_link_trg, plate_link_task, plates "
                        + "    WHERE link_job="+IdJob+" AND task_src_link_trg.link_task=id_task AND "
                        + "    plate_link_task.link_task=id_src_link_trg AND plate_link_task.link_plate=id_plate "
                        + ")";

                stat = conn.prepareStatement(SQLVal);

       //         stat.setInt(1, IdJob);
                stat.executeUpdate();

                stat.close();

            }

            // plate_link_task
            SQLVal = "DELETE FROM plate_link_task "
                    + "WHERE link_task IN ( "
                    + "    SELECT id_src_link_trg "
                    + "    FROM task_src_link_trg "
                    + "    WHERE link_task IN "
                    + "    ( "
                    + "        SELECT id_task FROM tasks "
                    + "        WHERE link_job="+IdJob+" "
                    + "    ) "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

        //    stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // level_link_task
            SQLVal = "DELETE FROM level_link_task "
                    + "WHERE link_task IN ( "
                    + "    SELECT id_src_link_trg "
                    + "    FROM task_src_link_trg "
                    + "    WHERE link_task IN "
                    + "    ( "
                    + "        SELECT id_task FROM tasks "
                    + "        WHERE link_job="+IdJob+" "
                    + "    ) "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

          //  stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // src_trg_link_csv
            SQLVal = "DELETE FROM src_trg_link_csv "
                    + "WHERE link_csv IN ( "
                    + "    SELECT id_src_link_trg "
                    + "    FROM task_src_link_trg "
                    + "    WHERE link_task IN "
                    + "    ( "
                    + "        SELECT id_task FROM tasks "
                    + "        WHERE link_job="+IdJob+" "
                    + "    ) "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

     //       stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // task_src_link_trg
            SQLVal = "DELETE FROM task_src_link_trg "
                    + " WHERE link_task IN "
                    + "( "
                    + "   SELECT id_task FROM tasks "
                    + "   WHERE link_job="+IdJob+" "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

        //    stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // task_csv_sequnce
            SQLVal = "DELETE FROM task_csv_sequence "
                    + "WHERE csv_link_task IN ( "
                    + "    SELECT id_task_csv_file "
                    + "    FROM task_csv_file "
                    + "    WHERE link_task IN "
                    + "    ( "
                    + "        SELECT id_task FROM tasks "
                    + "        WHERE link_job="+IdJob+" "
                    + "    ) "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

        //    stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // task_csv_file
            SQLVal = "DELETE FROM task_csv_file "
                    + "WHERE link_task IN "
                    + "( "
                    + "    SELECT id_task FROM tasks "
                    + "    WHERE link_job="+IdJob+" "
                    + ")";

            stat = conn.prepareStatement(SQLVal);

       //     stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // tasks
            SQLVal = "DELETE FROM tasks "
                    + " WHERE link_job=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();

            // jobs
            SQLVal = "DELETE FROM jobs "
                    + " WHERE id_job=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, IdJob);
            stat.executeUpdate();

            stat.close();
            conn.close();

        } catch (SQLException E) {

            System.out.println("Error Delete Job: " + E.getMessage());
            log.error("DeleteImportJob: " + E.getMessage());

        }

        return true;
    }

    public ObservableList<SettingsTableRecord> getSettings() {

        ObservableList<SettingsTableRecord> SettingsList = FXCollections.observableArrayList();

        String SQLVal = "";

        PreparedStatement stat = null;

        ResultSet resultset = null;

        Connection conn;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_setting, setting_name, setting_value, setting_unit FROM settings";

            stat = conn.prepareStatement(SQLVal);

            resultset = stat.executeQuery();

            while (resultset.next()) {

                SettingsTableRecord record = new SettingsTableRecord(
                        resultset.getInt("id_setting"),
                        resultset.getString("setting_name") + "(" + resultset.getString("setting_unit") + ")",
                        resultset.getString("setting_value"));

                SettingsList.add(record);

            }

            resultset.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("Error getSettings: " + E.getMessage());
            log.error("getSettings: " + E.getMessage());
        }

        return SettingsList;

    }

    public void UpdateSettings(int id, int value) {

        String SQLVal = "";

        PreparedStatement stat = null;

        Connection conn;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "UPDATE settings SET setting_value=? WHERE id_setting=?";

            stat = conn.prepareStatement(SQLVal);

            stat.setInt(1, value);
            stat.setInt(2, id);

            stat.executeUpdate();

            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("Error UpdateSettings: " + E.getMessage());
            log.error("UpdateSettings: " + E.getMessage());
        }

    }

    public boolean canSetSequence(int jobId) {

        PreparedStatement stat = null;

        ResultSet resultset = null;
        Connection conn;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            stat = conn.prepareStatement("SELECT job_types.job_type "
                    + "FROM jobs, job_types "
                    + "WHERE id_job=? AND jobs.job_type=id_job_type");

            stat.setInt(1, jobId);

            resultset = stat.executeQuery();

            String jobType = "";

            while (resultset.next()) {

                jobType = resultset.getString(1);

                break;

            }

            if (!jobType.equals("Job File")) {

                resultset.close();
                stat.close();
                conn.close();

                return false;
            }

            stat = conn.prepareStatement("SELECT task_status "
                    + "FROM tasks T, task_link_status, task_status "
                    + "WHERE link_job=? AND link_task=id_task AND link_status=id_task_status "
                    + "AND id_task_link_status IN "
                    + "( "
                    + "    SELECT max(id_task_link_status) "
                    + "    FROM task_link_status "
                    + "    WHERE link_task=T.id_task "
                    + ")");

            stat.setInt(1, jobId);

            resultset = stat.executeQuery();

            while (resultset.next()) {

                if (resultset.getString("task_status").equals("Running")) {

                    resultset.close();
                    stat.close();
                    conn.close();

                    return false;

                }

            }

            stat = conn.prepareStatement("SELECT task_types.task_type, task_status.task_status "
                    + "FROM tasks T, task_link_status, task_status, jobs, task_types, job_types "
                    + "WHERE link_job="+jobId+" AND done=0 AND job_types.job_type='Job File' AND "
                    + "T.task_type=task_types.id_task_type AND "
                    + "link_task=id_task AND link_status=id_task_status "
                    + "AND id_task_link_status IN "
                    + "( "
                    + "    SELECT max(id_task_link_status) "
                    + "    FROM task_link_status "
                    + "    WHERE link_task=T.id_task "
                    + ")");

//            stat.setInt(1, jobId);
//            stat.setString(2, "Job File");

            resultset = stat.executeQuery();

            boolean isExists = false;

            while (resultset.next()) {

                System.out.println(resultset.getString(1) + " - " + resultset.getString(2));

                if (resultset.getString(1).equals("Job File") && resultset.getString(2).equals("Done")) {

                    isExists = true;

                } else if (resultset.getString(1).equals("Job File") && resultset.getString(2).equals("Error")) {

                    isExists = true;

                } else if (resultset.getString(1).equals("Job File") && resultset.getString(2).equals("Canceled")) {

                    isExists = true;

                } else if (resultset.getString(1).equals("Job File") && resultset.getString(2).equals("Halted")) {

                    isExists = true;

                } else if (resultset.getString(1).equals("Job File") && resultset.getString(2).equals("Paused")) {

                    isExists = true;

                } else if (resultset.getString(1).equals("Job File") && resultset.getString(2).equals("Stopped")) {

                    isExists = true;

                }

                if (isExists) {
                    break;
                }

            }

            resultset.close();
            stat.close();

            if (isExists) {

                conn.close();

                return false;

            }

            conn.close();

        } catch (Exception E) {
            System.out.println("Error canSetSequence: " + E.getMessage());
            log.error("canSetSequence: " + E.getMessage());
        }

        return true;

    }

    public int getJobSequence(int idJob) {

        PreparedStatement stat = null;

        ResultSet resultset = null;

        Connection conn;

        int sequence = 0;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            stat = conn.prepareStatement("SELECT job_sequence FROM jobs WHERE id_job=?");
            stat.setInt(1, idJob);

            resultset = stat.executeQuery();

            while (resultset.next()) {
                sequence = resultset.getInt(1);
            }

            resultset.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("Error getJobSequence: " + E.getMessage());
            log.error("getJobSequence: " + E.getMessage());
        }

        return sequence;

    }

    public int getPickID(String idJob) {
     
        
        PreparedStatement stat = null;

        ResultSet resultset = null;

        Connection conn;

        int idPick = 0;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            stat = conn.prepareStatement("select id_task, ID_JOB from tasks inner join jobs on tasks.LINK_JOB = jobs.ID_JOB where id_job =? and task_type = 10");
            stat.setInt(1, Integer.parseInt(idJob));

            resultset = stat.executeQuery();

            while (resultset.next()) {
                idPick = resultset.getInt(1);
            }

            resultset.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("Error getPickID: " + E.getMessage());
            log.error("getPickID: " + E.getMessage());
        }

        return idPick;
        
    }

    public String getjobId(int idTask) {
         PreparedStatement stat = null;

        ResultSet resultset = null;

        Connection conn;

        int idPick = 0;

        try {

            conn = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            stat = conn.prepareStatement("select  ID_JOB from tasks inner join jobs on tasks.LINK_JOB = jobs.ID_JOB where id_task=?");
            stat.setInt(1, idTask);

            resultset = stat.executeQuery();

            while (resultset.next()) {
                idPick = resultset.getInt(1);
            }

            resultset.close();
            stat.close();
            conn.close();

        } catch (Exception E) {
            System.out.println("Error getPickID: " + E.getMessage());
            log.error("getPickID: " + E.getMessage());
        }

        return Integer.toString(idPick);
        
    }

}
