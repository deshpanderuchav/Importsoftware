package com.liconic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;

public class DBTimer {

    private String DBpath;
    private String DBuser;
    private String DBpassword;

    private Logger log;

    private DateFormat df = new SimpleDateFormat("HH:mm MM.dd.yyyy");

    public DBTimer(String DBdriver, String DBpath, String DBuser, String DBpassword, Logger log) throws Exception {

        this.DBpath = DBpath;
        this.DBuser = DBuser;
        this.DBpassword = DBpassword;
        this.log = log;

        Class.forName(DBdriver);

    }

    public synchronized String getWarupDelay() {

        String SQLVal = "";

        PreparedStatement prepstat = null;

        ResultSet resultset = null;

        Connection connection;

        String res = "";

        try {

            connection = DriverManager.getConnection(DBpath, DBuser, DBpassword);

            SQLVal = "SELECT id_task, task_property_value FROM jobs INNER JOIN tasks T "
                    + "ON link_job=id_job INNER JOIN task_link_status ON task_link_status.link_task=id_task AND id_task_link_status IN (   SELECT MAX(id_task_link_status)   "
                    + "FROM task_link_status   WHERE link_task=T.id_task ) INNER JOIN task_status ON link_status=id_task_status AND task_status.task_status='Pending'"
                    + "INNER JOIN task_link_property ON task_link_property.link_task=id_task INNER JOIN task_properties ON link_property=id_task_property AND task_property='Date To Run'"
                    + "WHERE done=0 ORDER BY task_property_value";

            prepstat = connection.prepareStatement(SQLVal);

//            prepstat.setString(0, "Pending");
//            prepstat.setString(1, "Date to run");

            resultset = prepstat.executeQuery();

            while (resultset.next()) {

                Date date = new Date(resultset.getTimestamp("task_property_value").getTime());
                res = df.format(date);

                break;
            }

            resultset.close();
            prepstat.close();
            connection.close();

        } catch (SQLException E) {

            System.out.println("Error getWarupDelay: " + E.getMessage());
            log.error("Error getWarupDelay: " + E.getMessage());
        }

        return res;
    }
}
