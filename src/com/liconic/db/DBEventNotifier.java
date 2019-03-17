package com.liconic.db;

import com.liconic.stages.ImportStage;

import org.firebirdsql.event.DatabaseEvent;
import org.firebirdsql.event.EventListener;
import org.firebirdsql.event.EventManager;
import org.firebirdsql.event.FBEventManager;
import org.firebirdsql.gds.impl.GDSType;
import org.apache.logging.log4j.Logger;

public class DBEventNotifier {

    private EventManager eventManagerPlateLinkLevel = null;
    private EventManager eventManagerBuffer = null;
    private EventManager eventSafeArea = null;
    private ImportStage importStage;
    private String host;
    private String database;
    private String user;
    private String pass;
    private Logger log;

    //private static final int TIMEOUT = 10000;
    public DBEventNotifier(String host, String database, String user, String pass, ImportStage importStage, Logger log) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.pass = pass;
        this.importStage = importStage;
        this.log = log;
    }

    public void run() {

        try {
            eventManagerPlateLinkLevel = new FBEventManager(GDSType.getType("PURE_JAVA"));

            eventManagerPlateLinkLevel.setHost(host);
            eventManagerPlateLinkLevel.setDatabase(database);
            eventManagerPlateLinkLevel.setUser(user);
            eventManagerPlateLinkLevel.setPassword(pass);

            eventManagerPlateLinkLevel.connect();

            eventManagerPlateLinkLevel.addEventListener("PLATE_LINK_LEVEL", new EventListener() {

                @Override
                public void eventOccurred(DatabaseEvent de) {

                    System.out.println("DB Event PLATELINKLEVEL");
                    log.info("DB Event PLATELINKLEVEL");

                    importStage.DrawCountEmptyLevels();
                    importStage.DrawCountEmptyRacks();
                }
            });

            System.out.println("DB Event listener PLATELINKLEVEL created");
            log.info("DB Event listener PLATELINKLEVEL created");

        } catch (Exception E) {
            System.out.println("ERROR Create DB Event listener PLATELINKLEVEL " + E.getMessage());
            log.error("Create DB Event listener PLATELINKLEVEL " + E.getMessage());
            eventManagerPlateLinkLevel = null;
        }

        try {

            eventManagerBuffer = new FBEventManager(GDSType.getType("PURE_JAVA"));

            eventManagerBuffer.setHost(host);
            eventManagerBuffer.setDatabase(database);
            eventManagerBuffer.setUser(user);
            eventManagerBuffer.setPassword(pass);

            eventManagerBuffer.connect();

            eventManagerBuffer.addEventListener("BUFFER", new EventListener() {

                @Override
                public void eventOccurred(DatabaseEvent de) {

                    System.out.println("DB Event BUFFER");
                    log.info("DB Event BUFFER");

                    importStage.DrawBuffer();
                }
            });

            System.out.println("DB Event listener BUFFER created");
            log.info("DB Event listener BUFFER created");

        } catch (Exception E) {

            System.out.println("ERROR Create DB Event listener BUFFER " + E.getMessage());
            log.error("Create DB Event listener BUFFER " + E.getMessage());
            eventManagerBuffer = null;
        }

        try {

            eventSafeArea = new FBEventManager(GDSType.getType("PURE_JAVA"));

            eventSafeArea.setHost(host);
            eventSafeArea.setDatabase(database);
            eventSafeArea.setUser(user);
            eventSafeArea.setPassword(pass);

            eventSafeArea.connect();

            eventSafeArea.addEventListener("SAFE_AREA", new EventListener() {

                @Override
                public void eventOccurred(DatabaseEvent de) {

                    System.out.println("DB Event SAFE_AREA");
                    log.info("DB Event SAFE_AREA");

                    importStage.DrawExceptionPlates();
                }
            });

            System.out.println("DB Event listener SAFE_AREA created");
            log.info("DB Event listener SAFE_AREA created");

        } catch (Exception E) {

            System.out.println("ERROR Create DB Event listener SAFE_AREA " + E.getMessage());
            log.error("Create DB Event listener SAFE_AREA " + E.getMessage());
            eventSafeArea = null;
        }

    }

    public void stop() {
        try {

            if (eventManagerPlateLinkLevel != null) {
                eventManagerPlateLinkLevel.disconnect();
            }

            if (eventManagerBuffer != null) {
                eventManagerBuffer.disconnect();
            }

            if (eventSafeArea != null) {
                eventSafeArea.disconnect();
            }

        } catch (Exception E) {
            System.out.println("ERROR Close DB Event listener " + E.getMessage());
            log.error("ERROR Close DB Event listener " + E.getMessage());

        }
    }

}
