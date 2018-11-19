/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author GKo_W764
 */
public class SourceCreateDB {

    public static void main(String args[]) {
        createTables();
    }

    private static void createTables() {

        String DBpath = "jdbc:sqlserver://localhost:1433;databaseName=RegeneronSTC77k";
        String DBuser = "ADMIN";
        String DBpassword = "test";

        Connection connection;
        PreparedStatement prepstat = null;

        ResultSet rs = null;

        String SQLVal;

        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connection = DriverManager.getConnection(DBpath, DBuser, DBpassword);

////////////////////////////////////////////////////////////////////////////////            
// Table SYSTEMS  
            SQLVal = "CREATE TABLE SYSTEMS( "
                    + "ID_SYSTEM INT NOT NULL IDENTITY(1,1), "
                    + "SYSTEM_ID VARCHAR(20) NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE SYSTEMS ADD CONSTRAINT PK_SYSTEMS PRIMARY KEY (ID_SYSTEM)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT SYSTEMS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////            
// Table DEVICES  
            SQLVal = "CREATE TABLE DEVICES( "
                    + "ID_DEVICE INTEGER NOT NULL IDENTITY(1,1), "
                    + "DEVICE_ID VARCHAR(20) NOT NULL, "
                    + "DEVICE_SYSTEM INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE DEVICES ADD CONSTRAINT PK_DEVICES PRIMARY KEY (ID_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT DEVICES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY            
            SQLVal = "ALTER TABLE DEVICES ADD CONSTRAINT FK_DEVICES FOREIGN KEY (DEVICE_SYSTEM) REFERENCES SYSTEMS (ID_SYSTEM)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////            
// Table DEVICE_PARAMS  
            SQLVal = "CREATE TABLE DEVICE_PARAMS( "
                    + "ID_PARAM INTEGER NOT NULL IDENTITY(1,1), "
                    + "PARAM_NAME VARCHAR(20) NOT NULL, "
                    + "PARAM_INFO VARCHAR(30) DEFAULT '' NOT NULL, "
                    + "PARAM_TYPE INTEGER NOT NULL, "
                    + "PARAM_DATA_TYPE VARCHAR(6) DEFAULT '0' NOT NULL , "
                    + "PARAM_UNIT VARCHAR(10) NOT NULL DEFAULT '')";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE DEVICE_PARAMS ADD CONSTRAINT PK_DEVICE_PARAMS PRIMARY KEY (ID_PARAM)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT DEVICE_PARAMS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table PARAM_LINK_DEVICE  
            SQLVal = "CREATE TABLE PARAM_LINK_DEVICE( "
                    + "ID_PARAM_LINK_DEVICE INTEGER NOT NULL IDENTITY(1,1), "
                    + "LINK_PARAM INTEGER NOT NULL, "
                    + "LINK_DEVICE INTEGER NOT NULL, "
                    + "PARAM_VALUE VARCHAR(20))";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE PARAM_LINK_DEVICE ADD CONSTRAINT PK_PARAM_LINK_DEVICE PRIMARY KEY (ID_PARAM_LINK_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT PARAM_LINK_DEVICE ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE PARAM_LINK_DEVICE ADD CONSTRAINT FK_PARAM_LINK_DEVICE_DEVICE FOREIGN KEY (LINK_DEVICE) REFERENCES DEVICES (ID_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE PARAM_LINK_DEVICE ADD CONSTRAINT FK_PARAM_LINK_DEVICE_PARAM FOREIGN KEY (LINK_PARAM) REFERENCES DEVICE_PARAMS (ID_PARAM)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table CASSETTE_CONFIGURATION  
            SQLVal = "CREATE TABLE CASSETTE_CONFIGURATION ( "
                    + "ID_CASSETTE_CONF  INTEGER NOT NULL IDENTITY(1,1), "
                    + "TYPE_NUMBER       INTEGER NOT NULL, "
                    + "TYPE_NAME         VARCHAR(20) NOT NULL, "
                    + "LEVEL_HEIGHT      INTEGER NOT NULL, "
                    + "LEVEL_Z_PITCH     INTEGER NOT NULL, "
                    + "CASSETTE_HEIGHT   INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE CASSETTE_CONFIGURATION ADD CONSTRAINT PK_CASSETTE_CONFIGURATION PRIMARY KEY (ID_CASSETTE_CONF)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT CASSETTE_CONFIGURATION ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table CASSETTES  
            SQLVal = "CREATE TABLE CASSETTES ( "
                    + "ID_CASSETTE              INTEGER NOT NULL IDENTITY(1,1), "
                    + "CASSETTE_ID              INTEGER NOT NULL, "
                    + "CASSETTE_CONF            INTEGER NOT NULL, "
                    + "CASSETTE_DEVICE          INTEGER NOT NULL, "
                    + "CASSETTE_IS_SAFE_AREA    VARCHAR(1) DEFAULT 0 NOT NULL, "
                    + "CASSETTE_IS_BUFFER       VARCHAR(1) DEFAULT 0 NOT NULL, "
                    + "CASSETTE_BLOCKED         VARCHAR(1) DEFAULT 0 NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE CASSETTES ADD CONSTRAINT PK_CASSETTES PRIMARY KEY (ID_CASSETTE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT CASSETTES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE CASSETTES ADD CONSTRAINT FK_CASSETTE_DEVICE FOREIGN KEY (CASSETTE_DEVICE) REFERENCES DEVICES (ID_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE CASSETTES ADD CONSTRAINT FK_CASSETTE_CONFIG FOREIGN KEY (CASSETTE_CONF) REFERENCES CASSETTE_CONFIGURATION (ID_CASSETTE_CONF)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table RACK_CONFIGURATION 
            SQLVal = "CREATE TABLE RACK_CONFIGURATION ( "
                    + "ID_RACK_CONF    INTEGER NOT NULL IDENTITY(1,1), "
                    + "RACK_CONF_ID    INTEGER NOT NULL, "
                    + "RACK_CONF_NAME  VARCHAR(20) NOT NULL, "
                    + "RACK_HEIGHT     INTEGER NOT NULL, "
                    + "IS_INVERTED     VARCHAR(1) DEFAULT '0' NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE RACK_CONFIGURATION ADD CONSTRAINT PK_RACK_CONFIGURATION PRIMARY KEY (ID_RACK_CONF)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT RACK_CONFIGURATION ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table CASSETTES_TYPES_LINK_RACKS 
            SQLVal = "CREATE TABLE CASSETTES_TYPES_LINK_RACKS ( "
                    + "LINK_CASSETTE_TYPE  INTEGER NOT NULL, "
                    + "LINK_RACK_TYPE      INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY 
//1            
            SQLVal = "ALTER TABLE CASSETTES_TYPES_LINK_RACKS ADD CONSTRAINT FK_LINK_CASSETTE_TYPE FOREIGN KEY (LINK_CASSETTE_TYPE) REFERENCES CASSETTE_CONFIGURATION (ID_CASSETTE_CONF)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE CASSETTES_TYPES_LINK_RACKS ADD CONSTRAINT FK_LINK_RACK_TYPE FOREIGN KEY (LINK_RACK_TYPE) REFERENCES RACK_CONFIGURATION (ID_RACK_CONF)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table PLATES 
            SQLVal = "CREATE TABLE PLATES ( "
                    + "ID_PLATE         INTEGER NOT NULL IDENTITY(1,1), "
                    + "PLATE_UID        VARCHAR(30) DEFAULT '' NOT NULL, "
                    + "PLATE_BC         VARCHAR(30) DEFAULT '' NOT NULL, "
                    + "PLATE_CONF       INTEGER NOT NULL, "
                    + "PLATE_PRELOADED  VARCHAR(1) DEFAULT 0 NOT NULL )";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE PLATES ADD CONSTRAINT PK_PLATES PRIMARY KEY (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT PLATES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE PLATES ADD CONSTRAINT FK_PLATE_CONF FOREIGN KEY (PLATE_CONF) REFERENCES RACK_CONFIGURATION (ID_RACK_CONF)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Index plate_bc
            SQLVal = "CREATE INDEX idx_plate_bc ON PLATES (PLATE_BC)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Index plate_uid
            SQLVal = "CREATE INDEX idx_plate_uid ON PLATES (PLATE_UID)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table LEVELS             
            SQLVal = "CREATE TABLE LEVELS ( "
                    + "ID_LEVEL         INTEGER NOT NULL IDENTITY(1,1), "
                    + "LEVEL_CASSETTE   INTEGER NOT NULL, "
                    + "LEVEL_ID         INTEGER NOT NULL, "
                    + "LEVEL_DEVICE_ID  INTEGER NOT NULL, "
                    + "LEVEL_BLOCKED    VARCHAR(1) DEFAULT 0 NOT NULL, "
                    + "LINK_PLATE       INTEGER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE LEVELS ADD CONSTRAINT PK_LEVELS PRIMARY KEY (ID_LEVEL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT LEVELS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE LEVELS ADD CONSTRAINT FK_LEVEL_CASSETTE FOREIGN KEY (LEVEL_CASSETTE) REFERENCES CASSETTES (ID_CASSETTE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE LEVELS ADD CONSTRAINT FK_LEVEL_PLATE FOREIGN KEY (LINK_PLATE) REFERENCES PLATES (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table SHOVELS            
            SQLVal = "CREATE TABLE SHOVELS ( "
                    + "ID_SHOVEL      INTEGER NOT NULL IDENTITY(1,1), "
                    + "SHOVEL_NAME    VARCHAR(30) DEFAULT '' NOT NULL, "
                    + "SHOVEL_DEVICE  INTEGER NOT NULL, "
                    + "LINK_PLATE     INTEGER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE SHOVELS ADD CONSTRAINT PK_SHOVELS PRIMARY KEY (ID_SHOVEL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT SHOVELS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE SHOVELS ADD CONSTRAINT FK_SHOVEL_DEVICE FOREIGN KEY (SHOVEL_DEVICE) REFERENCES DEVICES (ID_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE SHOVELS ADD CONSTRAINT FK_SHOVEL_PLATE FOREIGN KEY (LINK_PLATE) REFERENCES PLATES (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table XFERS            
            SQLVal = "CREATE TABLE XFERS ( "
                    + "ID_XFER      INTEGER NOT NULL IDENTITY(1,1), "
                    + "XFER_NAME    VARCHAR(30) DEFAULT '' NOT NULL, "
                    + "XFER_DEVICE  INTEGER NOT NULL, "
                    + "LINK_PLATE     INTEGER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE XFERS ADD CONSTRAINT PK_XFERS PRIMARY KEY (ID_XFER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT XFERS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE XFERS ADD CONSTRAINT FK_XFER_DEVICE FOREIGN KEY (XFER_DEVICE) REFERENCES DEVICES (ID_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE XFERS ADD CONSTRAINT FK_XFER_PLATE FOREIGN KEY (LINK_PLATE) REFERENCES PLATES (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TUBES            
            SQLVal = "CREATE TABLE TUBES ( "
                    + "ID_TUBE       INTEGER NOT NULL IDENTITY(1,1), "
                    + "TUBE_UID      VARCHAR(30) DEFAULT '' NOT NULL, "
                    + "TUBE_BC       VARCHAR(30) DEFAULT '' NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TUBES ADD CONSTRAINT PK_TUBES PRIMARY KEY (ID_TUBE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT TUBES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Index tube_bc
            SQLVal = "CREATE INDEX idx_tube_bc ON TUBES (TUBE_BC)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Index tube_uid
            SQLVal = "CREATE INDEX idx_tube_uid ON TUBES (TUBE_UID)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TUBES_TYPES            
            SQLVal = "CREATE TABLE TUBES_TYPES ( "
                    + "ID_TUBE_TYPE      INTEGER NOT NULL IDENTITY(1,1), "
                    + "TUBE_TYPE_NAME    VARCHAR(30) DEFAULT '' NOT NULL, "
                    + "TUBE_HIGH         SMALLINT, "
                    + "TUBE_WIDTH        SMALLINT, "
                    + "TUBE_TYPE         SMALLINT NOT NULL, "
                    + "DEVICE_TUBE_TYPE  SMALLINT DEFAULT 0 NOT NULL, "
                    + "TUBE_TYPE_INFO    VARCHAR(200) DEFAULT '' NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TUBES_TYPES ADD CONSTRAINT PK_TUBE_TYPE PRIMARY KEY (ID_TUBE_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT TUBES_TYPES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TUBE_POSITIONS            
            SQLVal = "CREATE TABLE TUBE_POSITIONS ( "
                    + "ID_TUBE_POSITION  INTEGER NOT NULL IDENTITY(1,1), "
                    + "TUBE_X            INTEGER NOT NULL, "
                    + "TUBE_Y            INTEGER NOT NULL, "
                    + "TUBE_YA           VARCHAR(1) NOT NULL, "
                    + "TUBE_POS_TYPE     INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TUBE_POSITIONS ADD CONSTRAINT PK_TUBE_POSITION PRIMARY KEY (ID_TUBE_POSITION)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT TUBE_POSITIONS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE TUBE_POSITIONS ADD CONSTRAINT FK_TUBE_POS_TYPE FOREIGN KEY (TUBE_POS_TYPE) REFERENCES TUBES_TYPES (ID_TUBE_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table PLATE_LINK_TUBE_POS            
            SQLVal = "CREATE TABLE PLATE_LINK_TUBE_POS ( "
                    + "ID_LINK_TUBE_POS  INTEGER NOT NULL IDENTITY(1,1), "
                    + "LINK_PLATE        INTEGER NOT NULL, "
                    + "LINK_TUBE_POS     INTEGER NOT NULL, "
                    + "LINK_TUBE         INTEGER )";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE PLATE_LINK_TUBE_POS ADD CONSTRAINT PK_LINK_TUBE_POS PRIMARY KEY (ID_LINK_TUBE_POS)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT PLATE_LINK_TUBE_POS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE PLATE_LINK_TUBE_POS ADD CONSTRAINT FK_PLATE_TUBE_POS_PLATE FOREIGN KEY (LINK_PLATE) REFERENCES PLATES (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE PLATE_LINK_TUBE_POS ADD CONSTRAINT FK_PLATE_TUBE_POS FOREIGN KEY (LINK_TUBE_POS) REFERENCES TUBE_POSITIONS (ID_TUBE_POSITION)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//3            
            SQLVal = "ALTER TABLE PLATE_LINK_TUBE_POS ADD CONSTRAINT FK_PLATE_TUBE_POS_TUBE FOREIGN KEY (LINK_TUBE) REFERENCES TUBES (ID_TUBE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table PLATE_TYPES_LINK_TUBES            
            SQLVal = "CREATE TABLE PLATE_TYPES_LINK_TUBES ( "
                    + "ID_PLATE_TYPES_TUBES INTEGER NOT NULL IDENTITY(1,1), "
                    + "LINK_PLATE_TYPE  INTEGER NOT NULL, "
                    + "LINK_TUBE_TYPE   INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE PLATE_TYPES_LINK_TUBES ADD CONSTRAINT PK_PLATE_TYPES_TUBES PRIMARY KEY (ID_PLATE_TYPES_TUBES)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT PLATE_TYPES_LINK_TUBES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE PLATE_TYPES_LINK_TUBES ADD CONSTRAINT FK_PLATE_TYPES_TUBES_PLATES FOREIGN KEY (LINK_PLATE_TYPE) REFERENCES RACK_CONFIGURATION (ID_RACK_CONF)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE PLATE_TYPES_LINK_TUBES ADD CONSTRAINT FK_PLATE_TYPES_TUBES_TUBES FOREIGN KEY (LINK_TUBE_TYPE) REFERENCES TUBES_TYPES (ID_TUBE_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table CONTENT_PROPERTIES             
            SQLVal = "CREATE TABLE CONTENT_PROPERTIES ( "
                    + "ID_CONTENT_PROPERTY  INTEGER NOT NULL IDENTITY(1,1), "
                    + "CONTENT_PROPERTY     VARCHAR(20) DEFAULT '' NOT NULL, "
                    + "CONTENT_UNIT         VARCHAR(5) DEFAULT '' NOT NULL, "
                    + "CONTENT_TYPE INTEGER DEFAULT 0 NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE CONTENT_PROPERTIES ADD CONSTRAINT PK_CONTENT_PROPERTY PRIMARY KEY (ID_CONTENT_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT CONTENT_PROPERTIES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table PROPERTY_LINK_PLATE            
            SQLVal = "CREATE TABLE PROPERTY_LINK_PLATE ( "
                    + "LINK_PLATE INTEGER NOT NULL, "
                    + "LINK_PROPERTY INTEGER NOT NULL, "
                    + "PROPERTY_VALUE  VARCHAR(256) NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE PROPERTY_LINK_PLATE ADD CONSTRAINT FK_PROP_LINK_PLATE_PLATE FOREIGN KEY (LINK_PLATE) REFERENCES PLATES (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE PROPERTY_LINK_PLATE ADD CONSTRAINT FK_PROP_LINK_PLATE_PROP FOREIGN KEY (LINK_PROPERTY) REFERENCES CONTENT_PROPERTIES (ID_CONTENT_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table PROPERTY_LINK_TUBE            
            SQLVal = "CREATE TABLE PROPERTY_LINK_TUBE ( "
                    + "LINK_TUBE INTEGER NOT NULL, "
                    + "LINK_PROPERTY INTEGER NOT NULL, "
                    + "PROPERTY_VALUE  VARCHAR(256) NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE PROPERTY_LINK_TUBE ADD CONSTRAINT FK_PROP_LINK_TUBE_TUBE FOREIGN KEY (LINK_TUBE) REFERENCES TUBES (ID_TUBE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE PROPERTY_LINK_TUBE ADD CONSTRAINT FK_PROP_LINK_TUBE_PROP FOREIGN KEY (LINK_PROPERTY) REFERENCES CONTENT_PROPERTIES (ID_CONTENT_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_EVENTS             
            SQLVal = "CREATE TABLE HISTORY_EVENTS ( "
                    + "ID_HISTORY_EVENTS   SMALLINT NOT NULL IDENTITY(1,1), "
                    + "HISTORY_EVENT_NAME  VARCHAR(30) NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE HISTORY_EVENTS ADD CONSTRAINT PK_HISTORY_EVENT PRIMARY KEY (ID_HISTORY_EVENTS)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT HISTORY_EVENTS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table USERS             
            SQLVal = "CREATE TABLE USERS ( "
                    + "ID_USER           INTEGER NOT NULL IDENTITY(1,1), "
                    + "USER_NAME         VARCHAR(30) NOT NULL, "
                    + "USER_ROLE         INTEGER DEFAULT 0 NOT NULL, "
                    + "USER_SECOND_NAME  VARCHAR(30), "
                    + "USER_LOGIN        VARCHAR(30) NOT NULL, "
                    + "USER_E_MAIL       VARCHAR(50), "
                    + "USER_DELETED      SMALLINT DEFAULT 0 NOT NULL, "
                    + "USER_E_MAIL_ACTION  SMALLINT DEFAULT 0 NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE USERS ADD CONSTRAINT PK_USERS PRIMARY KEY (ID_USER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT USERS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY            
            SQLVal = "CREATE TABLE HISTORY ( "
                    + "ID_HISTORY     INTEGER NOT NULL IDENTITY(1,1), "
                    + "HISTORY_DATE   DATETIME NOT NULL, "
                    + "HISTORY_USER   INTEGER NOT NULL, "
                    + "HISTORY_EVENT  SMALLINT NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE HISTORY ADD CONSTRAINT PK_HISTORY PRIMARY KEY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT HISTORY ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY ADD CONSTRAINT FK_HISTORY_EVENT FOREIGN KEY (HISTORY_EVENT) REFERENCES HISTORY_EVENTS (ID_HISTORY_EVENTS)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY ADD CONSTRAINT FK_HISTORY_USER FOREIGN KEY (HISTORY_USER) REFERENCES USERS (ID_USER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_LINK_PLATE                
            SQLVal = "CREATE TABLE HISTORY_LINK_PLATE ( "
                    + "LINK_PLATE    INTEGER NOT NULL, "
                    + "LINK_HISTORY  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_LINK_PLATE ADD CONSTRAINT FK_HISTORY_LINK_PLATE FOREIGN KEY (LINK_PLATE) REFERENCES PLATES (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY_LINK_PLATE ADD CONSTRAINT FK_HISTORY_LINK_PLATE_HISTORY FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_LINK_LEVEL              
            SQLVal = "CREATE TABLE HISTORY_LINK_LEVEL ( "
                    + "LINK_LEVEL    INTEGER NOT NULL, "
                    + "LINK_HISTORY  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_LINK_LEVEL ADD CONSTRAINT FK_HISTORY_LINK_LEVEL FOREIGN KEY (LINK_LEVEL) REFERENCES LEVELS (ID_LEVEL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY_LINK_LEVEL ADD CONSTRAINT FK_HISTORY_LINK_LEVEL_HISTORY FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_LINK_TUBE              
            SQLVal = "CREATE TABLE HISTORY_LINK_TUBE ( "
                    + "LINK_TUBE     INTEGER NOT NULL, "
                    + "LINK_HISTORY  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_LINK_TUBE ADD CONSTRAINT FK_HISTORY_LINK_TUBE FOREIGN KEY (LINK_TUBE) REFERENCES TUBES (ID_TUBE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY_LINK_TUBE ADD CONSTRAINT FK_HISTORY_LINK_TUBE_HISTORY FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_LINK_TUBE_POS              
            SQLVal = "CREATE TABLE HISTORY_LINK_TUBE_POS ( "
                    + "LINK_TUBE_POS   INTEGER NOT NULL, "
                    + "LINK_HISTORY  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_LINK_TUBE_POS ADD CONSTRAINT FK_HISTORY_LINK_TUBE_POS_P FOREIGN KEY (LINK_TUBE_POS) REFERENCES PLATE_LINK_TUBE_POS (ID_LINK_TUBE_POS)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY_LINK_TUBE_POS ADD CONSTRAINT FK_HISTORY_LINK_TUBE_POS_H FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_LINK_SHOVEL             
            SQLVal = "CREATE TABLE HISTORY_LINK_SHOVEL ( "
                    + "LINK_SHOVEL  INTEGER NOT NULL, "
                    + "LINK_HISTORY  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_LINK_SHOVEL ADD CONSTRAINT FK_HISTORY_LINK_SHOVEL FOREIGN KEY (LINK_SHOVEL) REFERENCES SHOVELS (ID_SHOVEL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY_LINK_SHOVEL ADD CONSTRAINT FK_HISTORY_LINK_SHOVEL_HISTORY FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_LINK_XFER             
            SQLVal = "CREATE TABLE HISTORY_LINK_XFER ( "
                    + "LINK_XFER  INTEGER NOT NULL, "
                    + "LINK_HISTORY  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_LINK_XFER ADD CONSTRAINT FK_HISTORY_LINK_XFER FOREIGN KEY (LINK_XFER) REFERENCES XFERS (ID_XFER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY_LINK_XFER ADD CONSTRAINT FK_HISTORY_LINK_XFER_HISTORY FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table PARTITIONS            
            SQLVal = "CREATE TABLE PARTITIONS ( "
                    + "ID_PARTITION  INTEGER NOT NULL IDENTITY(1,1), "
                    + "PARTITION_ID  VARCHAR(30) NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE PARTITIONS ADD CONSTRAINT PK_PARTITIONS PRIMARY KEY (ID_PARTITION)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT PARTITIONS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table CASSETTE_LINK_PARTITION            
            SQLVal = "CREATE TABLE CASSETTE_LINK_PARTITION ( "
                    + "LINK_CASSETTE   INTEGER NOT NULL, "
                    + "LINK_PARTITION  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE CASSETTE_LINK_PARTITION ADD CONSTRAINT FK_CASSETTE_LINK_PARTITION_CASS FOREIGN KEY (LINK_CASSETTE) REFERENCES CASSETTES (ID_CASSETTE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE CASSETTE_LINK_PARTITION ADD CONSTRAINT FK_CASSETTE_LINK_PARTITION_PART FOREIGN KEY (LINK_PARTITION) REFERENCES PARTITIONS (ID_PARTITION)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table USER_LINK_PARTITION            
            SQLVal = "CREATE TABLE USER_LINK_PARTITION ( "
                    + "LINK_USER       INTEGER NOT NULL, "
                    + "LINK_PARTITION  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE USER_LINK_PARTITION ADD CONSTRAINT FK_USER_LINK_PARTITION_USER FOREIGN KEY (LINK_USER) REFERENCES USERS (ID_USER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE USER_LINK_PARTITION ADD CONSTRAINT FK_USER_LINK_PARTITION_PART FOREIGN KEY (LINK_PARTITION) REFERENCES PARTITIONS (ID_PARTITION)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table CLIMATE_HISTORY            
            SQLVal = "CREATE TABLE CLIMATE_HISTORY ( "
                    + "ID_CLIMATE_HISTORY   INTEGER NOT NULL IDENTITY(1,1), "
                    + "CLIMATE_VALUE        NUMERIC(15,2) NOT NULL, "
                    + "CLIMATE_LINK         INTEGER NOT NULL, "
                    + "CLIMATE_DATE         DATETIME NOT NULL, "
                    + "CLIMATE_DEVICE  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE CLIMATE_HISTORY ADD CONSTRAINT PK_CLIMATE_HISTORY PRIMARY KEY (ID_CLIMATE_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT CLIMATE_HISTORY ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE CLIMATE_HISTORY ADD CONSTRAINT FK_CLIMATE_LINK  FOREIGN KEY (CLIMATE_LINK) REFERENCES DEVICE_PARAMS (ID_PARAM)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE CLIMATE_HISTORY ADD CONSTRAINT FK_CLIMATE_DEVICE FOREIGN KEY (CLIMATE_DEVICE) REFERENCES DEVICES (ID_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table BCR
// Table
            SQLVal = "CREATE TABLE BCR ( "
                    + "ID_BCR      INTEGER NOT NULL IDENTITY(1,1), "
                    + "BCR_NAME    VARCHAR(20), "
                    + "BCR_DEVICE  INTEGER NOT NULL, "
                    + "BCR_PLATE   INTEGER, "
                    + "BCR_PORT    INTEGER DEFAULT 0 NOT NULL, "
                    + "BCR_CMD     VARCHAR(300) DEFAULT '' NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE BCR ADD CONSTRAINT PK_BCR PRIMARY KEY (ID_BCR)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT BCR ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE BCR ADD CONSTRAINT FK_BCR_1 FOREIGN KEY (BCR_DEVICE) REFERENCES DEVICES (ID_DEVICE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE BCR ADD CONSTRAINT FK_BCR_2 FOREIGN KEY (BCR_PLATE) REFERENCES PLATES (ID_PLATE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table BCR_TUBE_TYPES           
// Table
            SQLVal = "CREATE TABLE BCR_TUBE_TYPES ( "
                    + "ID_BCR_TUBE_TYPE  INTEGER NOT NULL IDENTITY(1,1), "
                    + "LINK_BCR          INTEGER NOT NULL, "
                    + "LINK_TUBE_TYPE    INTEGER NOT NULL, "
                    + "GROUP_NAME        VARCHAR(30) DEFAULT '' NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE BCR_TUBE_TYPES ADD PRIMARY KEY (ID_BCR_TUBE_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

            SQLVal = "SET IDENTITY_INSERT BCR_TUBE_TYPES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE BCR_TUBE_TYPES ADD CONSTRAINT FK_BCR_TUBE_TYPES_1 FOREIGN KEY (LINK_BCR) REFERENCES BCR (ID_BCR)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE BCR_TUBE_TYPES ADD CONSTRAINT FK_BCR_TUBE_TYPES_2 FOREIGN KEY (LINK_TUBE_TYPE) REFERENCES PLATE_TYPES_LINK_TUBES (ID_PLATE_TYPES_TUBES)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table HISTORY_LINK_BC             
            SQLVal = "CREATE TABLE HISTORY_LINK_BCR ( "
                    + "LINK_BCR    INTEGER NOT NULL, "
                    + "LINK_HISTORY  INTEGER NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_LINK_BCR ADD CONSTRAINT FK_HISTORY_LINK_BCR FOREIGN KEY (LINK_BCR) REFERENCES BCR (ID_BCR)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2            
            SQLVal = "ALTER TABLE HISTORY_LINK_BCR ADD CONSTRAINT FK_HISTORY_LINK_BCR_HISTORY FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table GEN_HISTORY_INVENTORY_ID
// Table
            SQLVal = "CREATE TABLE HISTORY_INVENTORY ( "
                    + "ID_HIST_INV       INTEGER NOT NULL IDENTITY(1,1), "
                    + "LINK_HISTORY      INTEGER NOT NULL, "
                    + "INVENTORY_TYPE    SMALLINT NOT NULL, "
                    + "INVENTORY_RESULT  SMALLINT NOT NULL, "
                    + "INVENTORY_BCR     VARCHAR(30) DEFAULT '' NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE HISTORY_INVENTORY ADD CONSTRAINT PK_HISTORY_INVENTORY PRIMARY KEY (ID_HIST_INV)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT HISTORY_INVENTORY ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE HISTORY_INVENTORY ADD CONSTRAINT FK_HISTORY_INVENTORY_1 FOREIGN KEY (LINK_HISTORY) REFERENCES HISTORY (ID_HISTORY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////// SCHEDULER ///////////////////////////////////            
////////////////////////////////////////////////////////////////////////////////             
// Table TASK_PROPERTIES
// Table
            SQLVal = "CREATE TABLE TASK_PROPERTIES ( "
                    + "    ID_TASK_PROPERTY  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    TASK_PROPERTY     VARCHAR(50) NOT NULL)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_PROPERTIES ADD CONSTRAINT PK_TASK_PROPERTIES PRIMARY KEY (ID_TASK_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASK_PROPERTIES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASK_TYPES
// Table
            SQLVal = "CREATE TABLE TASK_TYPES ( "
                    + "    ID_TASK_TYPE  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    TASK_TYPE     VARCHAR(20) NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_TYPES ADD CONSTRAINT PK_TASK_TYPES PRIMARY KEY (ID_TASK_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table JOB_TYPES          
// Table
            SQLVal = "CREATE TABLE JOB_TYPES ( "
                    + "    ID_JOB_TYPE  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    JOB_TYPE     VARCHAR(20) NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE JOB_TYPES ADD CONSTRAINT PK_JOB_TYPES PRIMARY KEY (ID_JOB_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT JOB_TYPES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASK_STATUS          
// Table
            SQLVal = "CREATE TABLE TASK_STATUS ( "
                    + "    ID_TASK_STATUS  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    TASK_STATUS     VARCHAR(20) NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_STATUS ADD CONSTRAINT PK_TASK_STATUS PRIMARY KEY (ID_TASK_STATUS)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASK_STATUS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table JOBS
// Table
            SQLVal = "CREATE TABLE JOBS ( "
                    + "    ID_JOB        INTEGER NOT NULL IDENTITY(1,1), "
                    + "    JOB_TYPE      INTEGER NOT NULL, "
                    + "    JOB_PRIORITY  SMALLINT DEFAULT 0 NOT NULL, "
                    + "    JOB_SEQUENCE  SMALLINT DEFAULT 1 NOT NULL, "
                    + "    JOB_ID        VARCHAR(50) DEFAULT '' NOT NULL, "
                    + "    DONE          VARCHAR(1) DEFAULT 0 NOT NULL, "
                    + "    LINK_USER     INTEGER NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE JOBS ADD CONSTRAINT PK_JOBS PRIMARY KEY (ID_JOB)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT JOBS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE JOBS ADD CONSTRAINT FK_JOBS_JOB_TYPE FOREIGN KEY (JOB_TYPE) REFERENCES JOB_TYPES (ID_JOB_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//2            
            SQLVal = "ALTER TABLE JOBS ADD CONSTRAINT FK_JOBS_LINK_USER FOREIGN KEY (LINK_USER) REFERENCES USERS (ID_USER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Index JOB_ID
            SQLVal = "CREATE INDEX idx_job_id ON JOBS (job_id)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table JOB_PROPERTIES          
// Table
            SQLVal = "CREATE TABLE JOB_PROPERTIES ( "
                    + "    ID_JOB_PROPERTY  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    JOB_PROPERTY     VARCHAR(255) NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE JOB_PROPERTIES ADD CONSTRAINT PK_JOB_PROPERTIES PRIMARY KEY (ID_JOB_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT JOB_PROPERTIES ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table JOB_LINK_PROPERTY
// Table
            SQLVal = "CREATE TABLE JOB_LINK_PROPERTY ( "
                    + "    ID_JOB_LINK_PROPERTY  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    LINK_JOB              INTEGER NOT NULL, "
                    + "    LINK_PROPERTY         INTEGER NOT NULL, "
                    + "    JOB_PROPERTY_VALUE    VARCHAR(50) DEFAULT '' NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE JOB_LINK_PROPERTY ADD CONSTRAINT PK_JOB_LINK_PROPERTY PRIMARY KEY (ID_JOB_LINK_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT JOB_LINK_PROPERTY ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE JOB_LINK_PROPERTY ADD CONSTRAINT FK_JOB_LINK_PROPERTY_JOB FOREIGN KEY (LINK_JOB) REFERENCES JOBS (ID_JOB) ON DELETE CASCADE ";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//2            
            SQLVal = "ALTER TABLE JOB_LINK_PROPERTY ADD CONSTRAINT FK_JOB_LINK_PROPERTY_PROP FOREIGN KEY (LINK_PROPERTY) REFERENCES JOB_PROPERTIES (ID_JOB_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASKS          
// Table
            SQLVal = "CREATE TABLE TASKS ( "
                    + "    ID_TASK        INTEGER NOT NULL IDENTITY(1,1), "
                    + "    TASK_USER      INTEGER NOT NULL, "
                    + "    TASK_TIME      DATETIME, "
                    + "    TASK_TYPE      INTEGER NOT NULL, "
                    + "    TASK_SEQUENCE  SMALLINT DEFAULT 1 NOT NULL, "
                    + "    TASK_PRIORITY  SMALLINT DEFAULT 0 NOT NULL, "
                    + "    LINK_JOB          INTEGER NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASKS ADD CONSTRAINT PK_TASKS PRIMARY KEY (ID_TASK)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASKS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE TASKS ADD CONSTRAINT FK_TASKS_USER FOREIGN KEY (TASK_USER) REFERENCES USERS (ID_USER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//2            
            SQLVal = "ALTER TABLE TASKS ADD CONSTRAINT FK_TASKS_TASK_TYPE FOREIGN KEY (TASK_TYPE) REFERENCES TASK_TYPES (ID_TASK_TYPE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//3            
            SQLVal = "ALTER TABLE TASKS ADD CONSTRAINT FK_TASK_JOB FOREIGN KEY (LINK_JOB) REFERENCES JOBS (ID_JOB) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASK_LINK_STATUS
// Table
            SQLVal = "CREATE TABLE TASK_LINK_STATUS ( "
                    + "    ID_TASK_LINK_STATUS  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    LINK_TASK            INTEGER NOT NULL, "
                    + "    LINK_STATUS          INTEGER NOT NULL, "
                    + "    STATUS_USER          INTEGER NOT NULL, "
                    + "    STATUS_TIME          DATETIME NOT NULL, "
                    + "    NOTE                 VARCHAR(50) DEFAULT '' NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_LINK_STATUS ADD CONSTRAINT PK_TASK_LINK_STATUS PRIMARY KEY (ID_TASK_LINK_STATUS)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASK_LINK_STATUS ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE TASK_LINK_STATUS ADD CONSTRAINT FK_TASK_LINK_STATUS_TASK FOREIGN KEY (LINK_TASK) REFERENCES TASKS (ID_TASK) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//2            
            SQLVal = "ALTER TABLE TASK_LINK_STATUS ADD CONSTRAINT FK_TASK_LINK_STATUS_STAT FOREIGN KEY (LINK_STATUS) REFERENCES TASK_STATUS (ID_TASK_STATUS)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//3            
            SQLVal = "ALTER TABLE TASK_LINK_STATUS ADD CONSTRAINT FK_TASK_LINK_STATUS_3 FOREIGN KEY (STATUS_USER) REFERENCES USERS (ID_USER)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASK_LINK_PROPERTY
// Table
            SQLVal = "CREATE TABLE TASK_LINK_PROPERTY ( "
                    + "    ID_TASK_LINK_PROPERTY  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    LINK_TASK              INTEGER NOT NULL, "
                    + "    LINK_PROPERTY          INTEGER NOT NULL, "
                    + "    TASK_PROPERTY_VALUE    VARCHAR(50) DEFAULT '' NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_LINK_PROPERTY ADD CONSTRAINT PK_TASK_LINK_PROPERTY PRIMARY KEY (ID_TASK_LINK_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASK_LINK_PROPERTY ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE TASK_LINK_PROPERTY ADD CONSTRAINT FK_TASK_LINK_PROPERTY_TASK FOREIGN KEY (LINK_TASK) REFERENCES TASKS (ID_TASK) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//2            
            SQLVal = "ALTER TABLE TASK_LINK_PROPERTY ADD CONSTRAINT FK_TASK_LINK_PROPERTY_PROP FOREIGN KEY (LINK_PROPERTY) REFERENCES TASK_PROPERTIES (ID_TASK_PROPERTY)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASK_SCV_FILE
// Table
            SQLVal = "CREATE TABLE TASK_CSV_FILE ( "
                    + "    ID_TASK_CSV_FILE  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    LINK_TASK         INTEGER NOT NULL, "
                    + "    FILE_NAME         VARCHAR(50) NOT NULL, "
                    + "    FILE_TYPE         SMALLINT DEFAULT 0 NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_CSV_FILE ADD CONSTRAINT PK_TASK_CSV_FILE PRIMARY KEY (ID_TASK_CSV_FILE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASK_CSV_FILE ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE TASK_CSV_FILE ADD CONSTRAINT FK_TASK_CSV_FILE_TASK FOREIGN KEY (LINK_TASK) REFERENCES TASKS (ID_TASK) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASK_CSV_SEQUENCE
// Table
            SQLVal = "CREATE TABLE TASK_CSV_SEQUENCE ( "
                    + "    ID_CSV_SEQUENCE  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    CSV_LINK_TASK    INTEGER NOT NULL, "
                    + "    SRC_PLATE_BCR    VARCHAR(20), "
                    + "    SRC_TUBE_BCR     VARCHAR(20), "
                    + "    SRC_X            VARCHAR(2), "
                    + "    SRC_Y            VARCHAR(2), "
                    + "    TRG_PLATE_BCR    VARCHAR(20), "
                    + "    TRG_X            VARCHAR(2), "
                    + "    TRG_Y            VARCHAR(2) "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_CSV_SEQUENCE ADD CONSTRAINT PK_TASK_CSV_SEQUENCE PRIMARY KEY (ID_CSV_SEQUENCE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASK_CSV_SEQUENCE ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE TASK_CSV_SEQUENCE ADD CONSTRAINT FK_TASK_CSV_SEQUENCE_FILE FOREIGN KEY (CSV_LINK_TASK) REFERENCES TASK_CSV_FILE (ID_TASK_CSV_FILE) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TASK_SRC_LINK_TRG
// Table
            SQLVal = "CREATE TABLE TASK_SRC_LINK_TRG ( "
                    + "    ID_SRC_LINK_TRG  INTEGER NOT NULL IDENTITY(1,1), "
                    + "    LINK_TASK        INTEGER NOT NULL, "
                    + "    SRC_TRG_RESULT   SMALLINT DEFAULT 0 NOT NULL, "
                    + "    SRC_TRG_DATA     INTEGER, "
                    + "    IS_DONE          SMALLINT DEFAULT 0 NOT NULL, "
                    + "    SRC_TRG_INFO     VARCHAR(50) DEFAULT '' NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Primary KEY
            SQLVal = "ALTER TABLE TASK_SRC_LINK_TRG ADD CONSTRAINT PK_TASK_SRC_LINK_TRG PRIMARY KEY (ID_SRC_LINK_TRG)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Trigger            
            SQLVal = "SET IDENTITY_INSERT TASK_SRC_LINK_TRG ON";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// Foreign KEY   
//1            
            SQLVal = "ALTER TABLE TASK_SRC_LINK_TRG ADD CONSTRAINT FK_TASK_SRC_LINK_TRG_TASK FOREIGN KEY (LINK_TASK) REFERENCES TASKS (ID_TASK) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table SRC_TRG_LINK_CSV       
// Table
            SQLVal = "CREATE TABLE SRC_TRG_LINK_CSV ( "
                    + "    LINK_SRC_TRG  INTEGER NOT NULL, "
                    + "    LINK_CSV      INTEGER NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//1            
            SQLVal = "ALTER TABLE SRC_TRG_LINK_CSV ADD CONSTRAINT FK_SRC_TRG_LINK_CSV_1 FOREIGN KEY (LINK_SRC_TRG) REFERENCES TASK_SRC_LINK_TRG (ID_SRC_LINK_TRG)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE SRC_TRG_LINK_CSV ADD CONSTRAINT FK_SRC_TRG_LINK_CSV_2 FOREIGN KEY (LINK_CSV) REFERENCES TASK_CSV_SEQUENCE (ID_CSV_SEQUENCE)";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table LEVEL_LINK_TASK       
// Table
            SQLVal = "CREATE TABLE LEVEL_LINK_TASK ( "
                    + "    LINK_LEVEL  INTEGER NOT NULL, "
                    + "    LINK_TASK   INTEGER NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//1            
            SQLVal = "ALTER TABLE LEVEL_LINK_TASK ADD CONSTRAINT FK_LEVEL_LINK_TASK_1 FOREIGN KEY (LINK_LEVEL) REFERENCES LEVELS (ID_LEVEL) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE LEVEL_LINK_TASK ADD CONSTRAINT FK_LEVEL_LINK_TASK_2 FOREIGN KEY (LINK_TASK) REFERENCES TASK_SRC_LINK_TRG (ID_SRC_LINK_TRG) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();
// 

////////////////////////////////////////////////////////////////////////////////             
// Table PLATE_LINK_TASK       
// Table
            SQLVal = "CREATE TABLE PLATE_LINK_TASK ( "
                    + "    LINK_PLATE  INTEGER NOT NULL, "
                    + "    LINK_TASK   INTEGER NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//1            
            SQLVal = "ALTER TABLE PLATE_LINK_TASK ADD CONSTRAINT FK_PLATE_LINK_TASK_1 FOREIGN KEY (LINK_PLATE) REFERENCES PLATES (ID_PLATE) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE PLATE_LINK_TASK ADD CONSTRAINT FK_PLATE_LINK_TASK_2 FOREIGN KEY (LINK_TASK) REFERENCES TASK_SRC_LINK_TRG (ID_SRC_LINK_TRG) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TUBE_POS_LINK_TASK       
// Table
            SQLVal = "CREATE TABLE TUBE_POS_LINK_TASK ( "
                    + "    LINK_TUBE_POS  INTEGER NOT NULL, "
                    + "    LINK_TASK      INTEGER NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//1            
            SQLVal = "ALTER TABLE TUBE_POS_LINK_TASK ADD CONSTRAINT FK_TUBE_POS_LINK_TASK_1 FOREIGN KEY (LINK_TUBE_POS) REFERENCES PLATE_LINK_TUBE_POS (ID_LINK_TUBE_POS) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE TUBE_POS_LINK_TASK ADD CONSTRAINT FK_TUBE_POS_LINK_TASK_2 FOREIGN KEY (LINK_TASK) REFERENCES TASK_SRC_LINK_TRG (ID_SRC_LINK_TRG) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

////////////////////////////////////////////////////////////////////////////////             
// Table TUBE_LINK_TASK       
// Table
            SQLVal = "CREATE TABLE TUBE_LINK_TASK ( "
                    + "    LINK_TUBE  INTEGER NOT NULL, "
                    + "    LINK_TASK  INTEGER NOT NULL "
                    + ")";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//1            
            SQLVal = "ALTER TABLE TUBE_LINK_TASK ADD CONSTRAINT FK_TUBE_LINK_TASK_1 FOREIGN KEY (LINK_TUBE) REFERENCES TUBES (ID_TUBE) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

//2
            SQLVal = "ALTER TABLE TUBE_LINK_TASK ADD CONSTRAINT FK_TUBE_LINK_TASK_2 FOREIGN KEY (LINK_TASK) REFERENCES TASK_SRC_LINK_TRG (ID_SRC_LINK_TRG) ON DELETE CASCADE";

            prepstat = connection.prepareStatement(SQLVal);

            prepstat.executeUpdate();

// 
////////////////////////////////////////////////////////////////////////////////              
////////////////////////////////   VALUES   ////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////              
            int sysID = 0;
// SYSTEM            

            SQLVal = "INSERT INTO SYSTEMS (system_id) VALUES (?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "KIWI");
            prepstat.executeUpdate();

            SQLVal = "SELECT MAX(id_system) FROM systems";
            prepstat = connection.prepareStatement(SQLVal);
            rs = prepstat.executeQuery();

            while (rs.next()) {
                sysID = rs.getInt(1);
                break;
            }

// Device Params
// Hardware            
            SQLVal = "INSERT INTO device_params (param_name, param_type) VALUES (?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Buffer");
            prepstat.setInt(2, 1);
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type) VALUES (?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Door");
            prepstat.setInt(2, 1);
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type) VALUES (?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "TubePicker");
            prepstat.setInt(2, 1);
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type) VALUES (?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Reformater");
            prepstat.setInt(2, 1);
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type) VALUES (?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Revolver");
            prepstat.setInt(2, 1);
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type) VALUES (?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Storage");
            prepstat.setInt(2, 1);
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type) VALUES (?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "DeviceGroup");
            prepstat.setInt(2, 1);
            prepstat.executeUpdate();

// Climate
            SQLVal = "INSERT INTO device_params (param_name, param_type, param_unit) VALUES (?,?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Temperature");
            prepstat.setInt(2, 2);
            prepstat.setString(3, "°C");
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type, param_unit) VALUES (?,?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Humidity");
            prepstat.setInt(2, 2);
            prepstat.setString(3, "%");
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type, param_unit) VALUES (?,?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Cassettes Shift");
            prepstat.setInt(2, 4);
            prepstat.setString(3, "");
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type, param_unit) VALUES (?,?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "ActiveExternalXfer");
            prepstat.setInt(2, 1);
            prepstat.setString(3, "");
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type, param_unit) VALUES (?,?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "TransferSelector");
            prepstat.setInt(2, 5);
            prepstat.setString(3, "");
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type, param_unit) VALUES (?,?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Inverted X");
            prepstat.setInt(2, 5);
            prepstat.setString(3, "");
            prepstat.executeUpdate();

            SQLVal = "INSERT INTO device_params (param_name, param_type, param_unit) VALUES (?,?,?)";
            prepstat = connection.prepareStatement(SQLVal);
            prepstat.setString(1, "Inverted Y");
            prepstat.setInt(2, 5);
            prepstat.setString(3, "");
            prepstat.executeUpdate();

// Devices
            int devID = 0;
            int paramID = 0;

// Buffer
            prepstat = connection.prepareStatement("INSERT INTO devices (device_id, device_system) VALUES (?,?)");
            prepstat.setString(1, "Buffer1");
            prepstat.setInt(2, sysID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX(id_device) FROM devices");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            // Parameters
            // IO
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Buffer");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // Door
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Door");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // Temperature
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Temperature");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "982");
            prepstat.executeUpdate();

            // Humidity
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Humidity");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "983");
            prepstat.executeUpdate();

            // DeviceGroup
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "DeviceGroup");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "Left");
            prepstat.executeUpdate();

            // ActiveExternalXfer
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "ActiveExternalXfer");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

            // Shovel    
            prepstat = connection.prepareStatement("INSERT INTO SHOVELS (shovel_name, shovel_device) VALUES (?,?)");
            prepstat.setString(1, "Shovel 1");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // Xfer    
            prepstat = connection.prepareStatement("INSERT INTO XFERS (xfer_name, xfer_device) VALUES (?,?)");
            prepstat.setString(1, "External");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO XFERS (xfer_name, xfer_device) VALUES (?,?)");
            prepstat.setString(1, "Internal Upper");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO XFERS (xfer_name, xfer_device) VALUES (?,?)");
            prepstat.setString(1, "Internal Bottom");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

// TubePicker 1
            prepstat = connection.prepareStatement("INSERT INTO devices (device_id, device_system) VALUES (?,?)");
            prepstat.setString(1, "TubePicker1");
            prepstat.setInt(2, sysID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX(id_device) FROM devices");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            // Parameters                      
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "TubePicker");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Revolver");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // DeviceGroup
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "DeviceGroup");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "Left");
            prepstat.executeUpdate();

            // TransferSelector
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "TransferSelector");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1104");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1106");
            prepstat.executeUpdate();

            // Inverted X
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Inverted X");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

            // Inverted Y
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Inverted Y");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

// TubePicker Reformater
            prepstat = connection.prepareStatement("INSERT INTO devices (device_id, device_system) VALUES (?,?)");
            prepstat.setString(1, "Reformater");
            prepstat.setInt(2, sysID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX(id_device) FROM devices");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            // Parameters            
            // STT
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "TubePicker");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Reformater");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // DeviceGroup
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "DeviceGroup");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "Left");
            prepstat.executeUpdate();

            // TransferSelector
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "TransferSelector");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1104");
            prepstat.executeUpdate();

            // Inverted X
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Inverted X");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

            // Inverted Y
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Inverted Y");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

            // Storage
            prepstat = connection.prepareStatement("INSERT INTO devices (device_id, device_system) VALUES (?,?)");
            prepstat.setString(1, "Storage1");
            prepstat.setInt(2, sysID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX(id_device) FROM devices");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            // Parameters               
            // Storage
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Storage");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // DeviceGroup
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "DeviceGroup");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "Left");
            prepstat.executeUpdate();

            // Temperature
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Temperature");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "980");
            prepstat.executeUpdate();

            // Humidity
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Humidity");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "983");
            prepstat.executeUpdate();

            // Cassettes Shift
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Cassettes Shift");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "20");
            prepstat.executeUpdate();

            // Shovel                    
            prepstat = connection.prepareStatement("INSERT INTO SHOVELS (shovel_name, shovel_device) VALUES (?,?)");
            prepstat.setString(1, "Shovel 2");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

// The second system             
// Buffer
            prepstat = connection.prepareStatement("INSERT INTO devices (device_id, device_system) VALUES (?,?)");
            prepstat.setString(1, "Buffer2");
            prepstat.setInt(2, sysID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX(id_device) FROM devices");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            // Parameters
            // IO
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Buffer");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // Door
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Door");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // DeviceGroup
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "DeviceGroup");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "Right");
            prepstat.executeUpdate();

            // Temperature
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Temperature");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "982");
            prepstat.executeUpdate();

            // Humidity
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Humidity");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "983");
            prepstat.executeUpdate();

            // ActiveExternalXfer
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "ActiveExternalXfer");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

            // Shovel    
            prepstat = connection.prepareStatement("INSERT INTO SHOVELS (shovel_name, shovel_device) VALUES (?,?)");
            prepstat.setString(1, "Shovel 3");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // Xfer    
            prepstat = connection.prepareStatement("INSERT INTO XFERS (xfer_name, xfer_device) VALUES (?,?)");
            prepstat.setString(1, "External");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO XFERS (xfer_name, xfer_device) VALUES (?,?)");
            prepstat.setString(1, "Internal Upper");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO XFERS (xfer_name, xfer_device) VALUES (?,?)");
            prepstat.setString(1, "Internal Bottom");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

// TubePicker 2
            prepstat = connection.prepareStatement("INSERT INTO devices (device_id, device_system) VALUES (?,?)");
            prepstat.setString(1, "TubePicker2");
            prepstat.setInt(2, sysID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX(id_device) FROM devices");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            // Parameters            
            // STT
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "TubePicker");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Revolver");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // DeviceGroup
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "DeviceGroup");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "Right");
            prepstat.executeUpdate();

            // TransferSelector
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "TransferSelector");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1104");
            prepstat.executeUpdate();

            // Inverted X
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Inverted X");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

            // Inverted Y
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Inverted Y");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "1");
            prepstat.executeUpdate();

            // Storage
            prepstat = connection.prepareStatement("INSERT INTO devices (device_id, device_system) VALUES (?,?)");
            prepstat.setString(1, "Storage2");
            prepstat.setInt(2, sysID);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX(id_device) FROM devices");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            // Parameters               
            // Storage
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Storage");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device) VALUES (?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

            // DeviceGroup
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "DeviceGroup");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "Right");
            prepstat.executeUpdate();

            // Temperature
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Temperature");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "980");
            prepstat.executeUpdate();

            // Humidity
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Humidity");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "983");
            prepstat.executeUpdate();

            // Cassettes Shift
            prepstat = connection.prepareStatement("SELECT id_param FROM device_params WHERE param_name=?");
            prepstat.setString(1, "Cassettes Shift");

            rs = prepstat.executeQuery();

            while (rs.next()) {
                paramID = rs.getInt(1);
            }

            prepstat = connection.prepareStatement("INSERT INTO param_link_device (link_param, link_device, param_value) VALUES (?,?,?)");
            prepstat.setInt(1, paramID);
            prepstat.setInt(2, devID);
            prepstat.setString(3, "20");
            prepstat.executeUpdate();

            // Shovel                    
            prepstat = connection.prepareStatement("INSERT INTO SHOVELS (shovel_name, shovel_device) VALUES (?,?)");
            prepstat.setString(1, "Shovel 4");
            prepstat.setInt(2, devID);
            prepstat.executeUpdate();

////
// Cassettes Configuration
            prepstat = connection.prepareStatement("INSERT INTO CASSETTE_CONFIGURATION (type_number, type_name, level_height, level_z_pitch, cassette_height) VALUES (?,?,?,?,?)");
            prepstat.setInt(1, 0);
            prepstat.setString(2, "Buffer");
            prepstat.setInt(3, 63);
            prepstat.setInt(4, 0);
            prepstat.setInt(5, 0);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO CASSETTE_CONFIGURATION (type_number, type_name, level_height, level_z_pitch, cassette_height) VALUES (?,?,?,?,?)");
            prepstat.setInt(1, 0);
            prepstat.setString(2, "Nunc");
            prepstat.setInt(3, 57);
            prepstat.setInt(4, 0);
            prepstat.setInt(5, 0);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO CASSETTE_CONFIGURATION (type_number, type_name, level_height, level_z_pitch, cassette_height) VALUES (?,?,?,?,?)");
            prepstat.setInt(1, 0);
            prepstat.setString(2, "FluidX");
            prepstat.setInt(3, 25);
            prepstat.setInt(4, 0);
            prepstat.setInt(5, 0);
            prepstat.executeUpdate();

// Rack Configuration                
            prepstat = connection.prepareStatement("INSERT INTO RACK_CONFIGURATION (rack_conf_id, rack_conf_name, rack_height, is_inverted) VALUES (?,?,?,?)");
            prepstat.setInt(1, 0);
            prepstat.setString(2, "Unknown");
            prepstat.setInt(3, 0);
            prepstat.setInt(4, 0);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO RACK_CONFIGURATION (rack_conf_id, rack_conf_name, rack_height, is_inverted) VALUES (?,?,?,?)");
            prepstat.setInt(1, 1);
            prepstat.setString(2, "FliudX 0.3ml");
            prepstat.setInt(3, 25);
            prepstat.setInt(4, 0);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO RACK_CONFIGURATION (rack_conf_id, rack_conf_name, rack_height, is_inverted) VALUES (?,?,?,?)");
            prepstat.setInt(1, 2);
            prepstat.setString(2, "Nunc 1.8ml 48");
            prepstat.setInt(3, 57);
            prepstat.setInt(4, 0);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO RACK_CONFIGURATION (rack_conf_id, rack_conf_name, rack_height, is_inverted) VALUES (?,?,?,?)");
            prepstat.setInt(1, 3);
            prepstat.setString(2, "Nunc 1.8ml 60");
            prepstat.setInt(3, 57);
            prepstat.setInt(4, 0);
            prepstat.executeUpdate();

// Tubes Types
            int tubeTypeId = 0;

            // FliudX 0.3ml
            prepstat = connection.prepareStatement("INSERT INTO TUBES_TYPES (TUBE_TYPE_NAME, TUBE_HIGH, TUBE_WIDTH, TUBE_TYPE, DEVICE_TUBE_TYPE, TUBE_TYPE_INFO) VALUES (?,?,?,?,?,?)");
            prepstat.setString(1, "FliudX 0.3ml");
            prepstat.setInt(2, 20);
            prepstat.setInt(3, 8);
            prepstat.setInt(4, 1);
            prepstat.setInt(5, 2);
            prepstat.setString(6, "");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX (id_tube_type) FROM TUBES_TYPES");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                tubeTypeId = rs.getInt(1);
                break;
            }

            Set96Tubes(tubeTypeId, connection);

            // Nunc 1.8ml
            prepstat = connection.prepareStatement("INSERT INTO TUBES_TYPES (TUBE_TYPE_NAME, TUBE_HIGH, TUBE_WIDTH, TUBE_TYPE, DEVICE_TUBE_TYPE, TUBE_TYPE_INFO) VALUES (?,?,?,?,?,?)");
            prepstat.setString(1, "Nunc 1.8ml 48");
            prepstat.setInt(2, 50);
            prepstat.setInt(3, 10);
            prepstat.setInt(4, 2);
            prepstat.setInt(5, 0);
            prepstat.setString(6, "");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX (id_tube_type) FROM TUBES_TYPES");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                tubeTypeId = rs.getInt(1);
                break;
            }

            Set48Tubes(tubeTypeId, connection);

            prepstat = connection.prepareStatement("INSERT INTO TUBES_TYPES (TUBE_TYPE_NAME, TUBE_HIGH, TUBE_WIDTH, TUBE_TYPE, DEVICE_TUBE_TYPE, TUBE_TYPE_INFO) VALUES (?,?,?,?,?,?)");
            prepstat.setString(1, "Nunc 1.8ml 60");
            prepstat.setInt(2, 50);
            prepstat.setInt(3, 10);
            prepstat.setInt(4, 3);
            prepstat.setInt(5, 1);
            prepstat.setString(6, "");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT MAX (id_tube_type) FROM TUBES_TYPES");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                tubeTypeId = rs.getInt(1);
                break;
            }

            Set60Tubes(tubeTypeId, connection);

// Link cassettes types to racks
            // Buffer
            int cassetteTypeId = 0;

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Buffer");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            int plateType = 0;

            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "FliudX 0.3ml");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO CASSETTES_TYPES_LINK_RACKS (LINK_CASSETTE_TYPE, LINK_RACK_TYPE) VALUES (?,?)");
            prepstat.setInt(1, cassetteTypeId);
            prepstat.setInt(2, plateType);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 48");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO CASSETTES_TYPES_LINK_RACKS (LINK_CASSETTE_TYPE, LINK_RACK_TYPE) VALUES (?,?)");
            prepstat.setInt(1, cassetteTypeId);
            prepstat.setInt(2, plateType);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 60");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO CASSETTES_TYPES_LINK_RACKS (LINK_CASSETTE_TYPE, LINK_RACK_TYPE) VALUES (?,?)");
            prepstat.setInt(1, cassetteTypeId);
            prepstat.setInt(2, plateType);
            prepstat.executeUpdate();

            // Nunc            
            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Nunc");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 48");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO CASSETTES_TYPES_LINK_RACKS (LINK_CASSETTE_TYPE, LINK_RACK_TYPE) VALUES (?,?)");
            prepstat.setInt(1, cassetteTypeId);
            prepstat.setInt(2, plateType);
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 60");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO CASSETTES_TYPES_LINK_RACKS (LINK_CASSETTE_TYPE, LINK_RACK_TYPE) VALUES (?,?)");
            prepstat.setInt(1, cassetteTypeId);
            prepstat.setInt(2, plateType);
            prepstat.executeUpdate();

            // FluidX
            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "FluidX");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "FliudX 0.3ml");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO CASSETTES_TYPES_LINK_RACKS (LINK_CASSETTE_TYPE, LINK_RACK_TYPE) VALUES (?,?)");
            prepstat.setInt(1, cassetteTypeId);
            prepstat.setInt(2, plateType);
            prepstat.executeUpdate();

// Link tubes types to racks
            // Matrix
            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "FliudX 0.3ml");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_TUBE_TYPE FROM TUBES_TYPES WHERE TUBE_TYPE_NAME=?");
            prepstat.setString(1, "FliudX 0.3ml");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                tubeTypeId = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO PLATE_TYPES_LINK_TUBES (LINK_PLATE_TYPE, LINK_TUBE_TYPE) VALUES (?,?)");
            prepstat.setInt(1, plateType);
            prepstat.setInt(2, tubeTypeId);
            prepstat.executeUpdate();

            // Nunc
            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 48");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_TUBE_TYPE FROM TUBES_TYPES WHERE TUBE_TYPE_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 48");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                tubeTypeId = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO PLATE_TYPES_LINK_TUBES (LINK_PLATE_TYPE, LINK_TUBE_TYPE) VALUES (?,?)");
            prepstat.setInt(1, plateType);
            prepstat.setInt(2, tubeTypeId);
            prepstat.executeUpdate();

            // Nunc
            prepstat = connection.prepareStatement("SELECT ID_RACK_CONF FROM RACK_CONFIGURATION WHERE RACK_CONF_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 60");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                plateType = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_TUBE_TYPE FROM TUBES_TYPES WHERE TUBE_TYPE_NAME=?");
            prepstat.setString(1, "Nunc 1.8ml 60");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                tubeTypeId = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("INSERT INTO PLATE_TYPES_LINK_TUBES (LINK_PLATE_TYPE, LINK_TUBE_TYPE) VALUES (?,?)");
            prepstat.setInt(1, plateType);
            prepstat.setInt(2, tubeTypeId);
            prepstat.executeUpdate();

// Insert cassettes    
            // Buffer 1           
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "Buffer1");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Buffer");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            int devLevelID = 0;

            for (int i = 1; i <= 7; i++) {

                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");

                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();

                int cassetteID = 0;

                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while (rs.next()) {
                    cassetteID = rs.getInt(1);
                    break;
                }

                for (int l = 1; l <= 8; l++) {

                    devLevelID++;

                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");

                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);

                    prepstat.executeUpdate();
                }
            }

// BCR
            prepstat = connection.prepareStatement("INSERT INTO BCR (bcr_name, bcr_device, bcr_port, bcr_cmd) VALUES (?,?,?,?)");

            prepstat.setString(1, "2D Reader1");
            prepstat.setInt(2, devID);
            prepstat.setInt(3, 201);
            prepstat.setString(4, "C:\\Ziath.exe");

            prepstat.executeUpdate();

// Tube Picker
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "TubePicker1");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Nunc");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            devLevelID = 0;

            for (int i = 1; i <= 1; i++) {

                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");

                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();

                int cassetteID = 0;

                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while (rs.next()) {
                    cassetteID = rs.getInt(1);
                    break;
                }

                for (int l = 1; l <= 2; l++) {

                    devLevelID++;

                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");

                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);

                    prepstat.executeUpdate();
                }
            }

// Tube Picker
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "Reformater");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Nunc"); //"Buffer"
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            devLevelID = 0;

            for (int i = 1; i <= 1; i++) {

                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");

                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();

                int cassetteID = 0;

                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while (rs.next()) {
                    cassetteID = rs.getInt(1);
                    break;
                }

                for (int l = 1; l <= 2; l++) {

                    devLevelID++;

                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");

                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);

                    prepstat.executeUpdate();
                }
            }
// Storage            
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "Storage1");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Nunc");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            // Remp Cassettes
            for (int i = 1; i <= 387; i++) {

                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");

                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();

                int cassetteID = 0;

                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while (rs.next()) {
                    cassetteID = rs.getInt(1);
                    break;
                }

                for (int l = 1; l <= 27; l++) {

                    devLevelID++;

                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");

                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);

                    prepstat.executeUpdate();
                }
            }
//             

            // Buffer 2           
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "Buffer2");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Buffer");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            devLevelID = 0;

            for (int i = 1; i <= 7; i++) {

                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");

                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();

                int cassetteID = 0;

                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while (rs.next()) {
                    cassetteID = rs.getInt(1);
                    break;
                }

                for (int l = 1; l <= 8; l++) {

                    devLevelID++;

                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");

                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);

                    prepstat.executeUpdate();
                }
            }

// BCR
            prepstat = connection.prepareStatement("INSERT INTO BCR (bcr_name, bcr_device, bcr_port, bcr_cmd) VALUES (?,?,?,?)");

            prepstat.setString(1, "2D Reader2");
            prepstat.setInt(2, devID);
            prepstat.setInt(3, 201);
            prepstat.setString(4, "C:\\Ziath.exe");

            prepstat.executeUpdate();

// Tube Picker
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "TubePicker2");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "FluidX"); //"Buffer"
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            devLevelID = 0;

            for (int i = 1; i <= 1; i++) {

                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");

                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();

                int cassetteID = 0;

                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while (rs.next()) {
                    cassetteID = rs.getInt(1);
                    break;
                }

                for (int l = 1; l <= 2; l++) {

                    devLevelID++;

                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");

                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);

                    prepstat.executeUpdate();
                }
            }
            /*            
// Tube Picker
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "TubePicker3");
            rs = prepstat.executeQuery();
            
            while(rs.next()){
                devID = rs.getInt(1);
                break;
            }             
            
            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "Buffer");
            rs = prepstat.executeQuery();
            
            while(rs.next()){
                cassetteTypeId = rs.getInt(1);
                break;
            } 
            
            devLevelID = 0;
            
            for (int i=1; i<=1; i++){
                
                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");
                
                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();
                
                int cassetteID = 0;
                
                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while(rs.next()){
                    cassetteID = rs.getInt(1);
                    break;
                }                
                
                for (int l=1; l<=2; l++){
                    
                    devLevelID++;
                    
                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");
                    
                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);  
                    
                    prepstat.executeUpdate();
                }                
            }            
             */
// Storage            
            prepstat = connection.prepareStatement("SELECT id_device FROM devices WHERE device_id=?");
            prepstat.setString(1, "Storage2");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                devID = rs.getInt(1);
                break;
            }

            prepstat = connection.prepareStatement("SELECT ID_CASSETTE_CONF FROM CASSETTE_CONFIGURATION WHERE type_name=?");
            prepstat.setString(1, "FluidX");
            rs = prepstat.executeQuery();

            while (rs.next()) {
                cassetteTypeId = rs.getInt(1);
                break;
            }

            // Remp Cassettes
            for (int i = 1; i <= 387; i++) {

                prepstat = connection.prepareStatement("INSERT INTO cassettes (CASSETTE_ID, CASSETTE_CONF, CASSETTE_DEVICE, CASSETTE_IS_BUFFER) VALUES (?,?,?,?)");

                prepstat.setInt(1, i);
                prepstat.setInt(2, cassetteTypeId);
                prepstat.setInt(3, devID);
                prepstat.setInt(4, 1);

                prepstat.executeUpdate();

                int cassetteID = 0;

                prepstat = connection.prepareStatement("SELECT MAX(ID_CASSETTE) FROM CASSETTES");
                rs = prepstat.executeQuery();

                while (rs.next()) {
                    cassetteID = rs.getInt(1);
                    break;
                }

                for (int l = 1; l <= 62; l++) {

                    devLevelID++;

                    prepstat = connection.prepareStatement("INSERT INTO LEVELS (LEVEL_CASSETTE, LEVEL_ID, LEVEL_DEVICE_ID) VALUES (?,?,?)");

                    prepstat.setInt(1, cassetteID);
                    prepstat.setInt(2, l);
                    prepstat.setInt(3, devLevelID);

                    prepstat.executeUpdate();
                }
            }

// History Events            
            prepstat = connection.prepareStatement("INSERT INTO HISTORY_EVENTS (HISTORY_EVENT_NAME) VALUES (?)");
            prepstat.setString(1, "Load");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO HISTORY_EVENTS (HISTORY_EVENT_NAME) VALUES (?)");
            prepstat.setString(1, "Unload");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO HISTORY_EVENTS (HISTORY_EVENT_NAME) VALUES (?)");
            prepstat.setString(1, "Presence");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO HISTORY_EVENTS (HISTORY_EVENT_NAME) VALUES (?)");
            prepstat.setString(1, "Add");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO HISTORY_EVENTS (HISTORY_EVENT_NAME) VALUES (?)");
            prepstat.setString(1, "Delete");
            prepstat.executeUpdate();

// Contenet Properties
            prepstat = connection.prepareStatement("INSERT INTO CONTENT_PROPERTIES (CONTENT_PROPERTY, CONTENT_UNIT) VALUES (?,?)");
            prepstat.setString(1, "Matter");
            prepstat.setString(2, "");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO CONTENT_PROPERTIES (CONTENT_PROPERTY, CONTENT_UNIT) VALUES (?,?)");
            prepstat.setString(1, "Information");
            prepstat.setString(2, "");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO CONTENT_PROPERTIES (CONTENT_PROPERTY, CONTENT_UNIT) VALUES (?,?)");
            prepstat.setString(1, "Concentration");
            prepstat.setString(2, "%");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO CONTENT_PROPERTIES (CONTENT_PROPERTY, CONTENT_UNIT) VALUES (?,?)");
            prepstat.setString(1, "Volume");
            prepstat.setString(2, "ml");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO CONTENT_PROPERTIES (CONTENT_PROPERTY, CONTENT_UNIT) VALUES (?,?)");
            prepstat.setString(1, "Matter");
            prepstat.setString(2, "");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO CONTENT_PROPERTIES (CONTENT_PROPERTY, CONTENT_UNIT) VALUES (?,?)");
            prepstat.setString(1, "Filling");
            prepstat.setString(2, "ml");
            prepstat.executeUpdate();

// Add User
            prepstat = connection.prepareStatement("INSERT INTO users (user_name, user_role, user_second_name, user_login, user_e_mail, user_deleted) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");
            prepstat.setString(1, "ADMIN");
            prepstat.setInt(2, 1);
            prepstat.setString(3, "ADMIN");
            prepstat.setString(4, "ADMIN");
            prepstat.setString(5, "");
            prepstat.setInt(6, 0);

            prepstat.executeUpdate();

////////////////////////////////// SCHEDULER ///////////////////////////////////          
// Task types
            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "ImportBuffer");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Export");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Pick");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Inventory Plate");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Consolidate Plate");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Move Plate");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Move Tube");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Inventory Level");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "ImportXfer");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Job File");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Import Plate");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_types (task_type) VALUES (?)");
            prepstat.setString(1, "Scan 2D");
            prepstat.executeUpdate();

// Job Types
            prepstat = connection.prepareStatement("INSERT INTO job_types (job_type) VALUES (?)");
            prepstat.setString(1, "Import from Buffer");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_types (job_type) VALUES (?)");
            prepstat.setString(1, "Import");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_types (job_type) VALUES (?)");
            prepstat.setString(1, "Move Plate");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_types (job_type) VALUES (?)");
            prepstat.setString(1, "Job File");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_types (job_type) VALUES (?)");
            prepstat.setString(1, "Export");
            prepstat.executeUpdate();

// Task Status
            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Created");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Running");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Paused");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Halted");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Stopped");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Done");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Creating");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Pending");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Waiting");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Error");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Posponed");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO task_status (task_status) VALUES (?)");
            prepstat.setString(1, "Canceled");
            prepstat.executeUpdate();

// Job Property
            prepstat = connection.prepareStatement("INSERT INTO job_properties (job_property) VALUES (?)");
            prepstat.setString(1, "Target Partition");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_properties (job_property) VALUES (?)");
            prepstat.setString(1, "Consolidation");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_properties (job_property) VALUES (?)");
            prepstat.setString(1, "Verification");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_properties (job_property) VALUES (?)");
            prepstat.setString(1, "Empty Rack");
            prepstat.executeUpdate();

            prepstat = connection.prepareStatement("INSERT INTO job_properties (job_property) VALUES (?)");
            prepstat.setString(1, "Buffer delay");
            prepstat.executeUpdate();

            System.out.println("DONE");

        } catch (Exception E) {

            System.out.println("Error: " + E.getMessage());
        }

    }

    private static void Set96Tubes(int TubeType, Connection connection) {

        String YA = "";

        PreparedStatement prepstat;

        for (int x = 1; x <= 12; x++) {

            for (int y = 1; y <= 8; y++) {

                if (y == 1) {
                    YA = "A";
                } else if (y == 2) {
                    YA = "B";
                } else if (y == 3) {
                    YA = "C";
                } else if (y == 4) {
                    YA = "D";
                } else if (y == 5) {
                    YA = "E";
                } else if (y == 6) {
                    YA = "F";
                } else if (y == 7) {
                    YA = "G";
                } else if (y == 8) {
                    YA = "H";
                }

                try {

                    prepstat = connection.prepareStatement("INSERT INTO TUBE_POSITIONS (TUBE_X, TUBE_Y, TUBE_YA, TUBE_POS_TYPE) VALUES (?,?,?,?)");

                    prepstat.setInt(1, x);
                    prepstat.setInt(2, y);
                    prepstat.setString(3, YA);
                    prepstat.setInt(4, TubeType);

                    prepstat.executeUpdate();

                } catch (Exception E) {

                }

            }

        }

    }

    private static void Set48Tubes(int TubeType, Connection connection) {

        String YA = "";

        PreparedStatement prepstat;
        /*        
        for (int x=1; x<=12; x++){
            
            for (int y=1; y<=8; y++){
            
                if ((x % 2 == 0) != (y % 2 == 0)){

                    if (y == 1)
                       YA = "A";
                    else if (y == 2)
                       YA = "B";
                    else if (y == 3)
                       YA = "C";
                    else if (y == 4)
                       YA = "D";
                    else if (y == 5)
                       YA = "E";
                    else if (y == 6)
                       YA = "F";
                
                    try{
                
                        prepstat = connection.prepareStatement("INSERT INTO TUBE_POSITIONS (TUBE_X, TUBE_Y, TUBE_YA, TUBE_POS_TYPE) VALUES (?,?,?,?)");                                   
                
                        prepstat.setInt(1, x);
                        prepstat.setInt(2, y);
                        prepstat.setString(3, YA);
                        prepstat.setInt(4, TubeType);
                    
                        prepstat.executeUpdate();
                    
                    }catch(Exception E){
                    
                    }                      

                }
                
            }
            
        } 
         */

        for (int x = 1; x <= 8; x++) {

            for (int y = 1; y <= 6; y++) {

                if (y == 1) {
                    YA = "A";
                } else if (y == 2) {
                    YA = "B";
                } else if (y == 3) {
                    YA = "C";
                } else if (y == 4) {
                    YA = "D";
                } else if (y == 5) {
                    YA = "E";
                } else if (y == 6) {
                    YA = "F";
                }

                try {

                    prepstat = connection.prepareStatement("INSERT INTO TUBE_POSITIONS (TUBE_X, TUBE_Y, TUBE_YA, TUBE_POS_TYPE) VALUES (?,?,?,?)");

                    prepstat.setInt(1, x);
                    prepstat.setInt(2, y);
                    prepstat.setString(3, YA);
                    prepstat.setInt(4, TubeType);

                    prepstat.executeUpdate();

                } catch (Exception E) {

                }

            }

        }

    }

    private static void Set60Tubes(int TubeType, Connection connection) {

        String YA = "";

        PreparedStatement prepstat;

        for (int x = 1; x <= 17; x++) {

            for (int y = 1; y <= 7; y++) {

                if ((x % 2 == 0) == (y % 2 == 0)) {

                    if (y == 1) {
                        YA = "A";
                    } else if (y == 2) {
                        YA = "B";
                    } else if (y == 3) {
                        YA = "C";
                    } else if (y == 4) {
                        YA = "D";
                    } else if (y == 5) {
                        YA = "E";
                    } else if (y == 6) {
                        YA = "F";
                    } else if (y == 6) {
                        YA = "G";
                    }

                    try {

                        prepstat = connection.prepareStatement("INSERT INTO TUBE_POSITIONS (TUBE_X, TUBE_Y, TUBE_YA, TUBE_POS_TYPE) VALUES (?,?,?,?)");

                        prepstat.setInt(1, x);
                        prepstat.setInt(2, y);
                        prepstat.setString(3, YA);
                        prepstat.setInt(4, TubeType);

                        prepstat.executeUpdate();

                    } catch (Exception E) {

                    }
                }

            }

        }

    }

}
