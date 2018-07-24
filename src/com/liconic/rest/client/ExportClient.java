package com.liconic.rest.client;

import com.liconic.binding.sys.Cmd;
import com.liconic.binding.sys.Cmds;
import com.liconic.binding.sys.ObjectFactory;
import com.liconic.binding.sys.Sys;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.apache.logging.log4j.Logger;

public class ExportClient {

    private WebTarget webTarget;
       private Client client;
    private Logger log;

    private ObjectFactory of;

    public ExportClient(String url, Logger log) {

        this.log = log;

        try {
            client = ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("ExportClient create Client: " + E.getMessage());
        }

        try {
            webTarget = client.target(url).path("export");
        } catch (Exception E) {
            log.error("ExportClient create WebTarget: " + E.getMessage());
        }
    

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("ExportClient create ObjectFactory: " + E.getMessage());
        }

    }

    public Sys runExportRack(int idTask, int idRack, String UserId) {

        Sys sys = new Sys();
        Sys resp = null;

        try {

            log.info("run ExportRack");

            sys.setSysName(of.createSysSysName("KIWISystem1"));

            Cmds cmds = new Cmds();

            Cmd cmd = new Cmd();

            cmd.setType(of.createCmdType("ExportRack"));
            cmd.setId(String.valueOf(idTask));
            cmd.setUser(of.createCmdUser(UserId));

            if (idRack > 0) {
                cmd.setValue(of.createCmdValue(String.valueOf(idRack)));
            }

            cmds.getCmd().add(cmd);
            sys.setCmds(of.createSysCmds(cmds));

            resp = webTarget.request().put(Entity.xml(sys), Sys.class);

            System.out.println(resp.getSysName().getValue());
            log.info("runExportRack: " + resp.getSysName().getValue());

        } catch (Exception E) {
            System.out.println("Error: runExportRack: " + E.getMessage());
            log.error("runExportRack: " + E.getMessage());
        }

        return resp;

    }

    public Sys canceExportRack(int idJob, String UserId) throws Exception {

        Sys sys = new Sys();
        Sys resp = null;

        try {

            log.info("run CancelExport");

            sys.setSysName(of.createSysSysName("KIWISystem1"));

            Cmds cmds = new Cmds();

            Cmd cmd = new Cmd();

            cmd.setType(of.createCmdType("CancelExport"));
            cmd.setId(String.valueOf(idJob));
            cmd.setUser(of.createCmdUser(UserId));

            cmds.getCmd().add(cmd);
            sys.setCmds(of.createSysCmds(cmds));

            resp = webTarget.path("cancel").request().post(Entity.xml(sys), Sys.class);

            System.out.println(resp.getSysName().getValue());
            log.info("CancelExport: " + resp.getSysName().getValue());

        } catch (Exception E) {
            System.out.println("Error: CancelExport: " + E.getMessage());
            log.error("Error: CancelExport: " + E.getMessage());
            throw new Exception(E.getMessage());
        }

        return resp;

    }
}

