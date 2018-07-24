package com.liconic.rest.client;

import com.liconic.binding.sys.Cmd;
import com.liconic.binding.sys.Cmds;
import com.liconic.binding.sys.ObjectFactory;
import com.liconic.binding.sys.Sys;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.apache.logging.log4j.Logger;

public class ImportClient {

    private WebTarget webTarget;
    private Client client;
    private Logger log;

    private ObjectFactory of;

    public ImportClient(String url, Logger log) {

        this.log = log;

        try {
            client = javax.ws.rs.client.ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("ImportClient create Client: " + E.getMessage());
        }

        try {
            webTarget = client.target(url).path("import");
        } catch (Exception E) {
            log.error("ImportClient create WebTarget: " + E.getMessage());
        }

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("ImportClient create ObjectFactory: " + E.getMessage());
        }

    }

    public Sys runImportFromXfer() {

        Sys sys = new Sys();
        Sys resp = null;

        try {

            log.info("run ImportFromXfer");

            sys.setSysName(of.createSysSysName("KIWISystem1"));

            Cmds cmds = new Cmds();

            Cmd cmd = new Cmd();

            cmd.setType(of.createCmdType("ImportFromXfer"));

            cmds.getCmd().add(cmd);
            sys.setCmds(of.createSysCmds(cmds));

            resp = webTarget.request().post(Entity.xml(sys), Sys.class);

            System.out.println(resp.getSysName().getValue());
            log.info("runImportFromXfer: " + resp.getSysName().getValue());

        } catch (Exception E) {
            System.out.println("Error: runImportFromXfer: " + E.getMessage());
            log.error("runImportFromXfer: " + E.getMessage());
        }

        return resp;

    }

    public Sys runImportFromBuffer() {

        Sys sys = new Sys();
        Sys resp = null;

        try {

            log.info("run ImportFromBuffer");

            sys.setSysName(of.createSysSysName("KIWISystem1"));

            Cmds cmds = new Cmds();

            Cmd cmd = new Cmd();

            cmd.setType(of.createCmdType("ImportFromBuffer"));

            cmds.getCmd().add(cmd);
            sys.setCmds(of.createSysCmds(cmds));

            resp = webTarget.request().post(Entity.xml(sys), Sys.class);

            System.out.println(resp.getSysName().getValue());
            log.info("runImportFromBuffer: " + resp.getSysName().getValue());

        } catch (Exception E) {
            System.out.println("Error: runImportFromBuffer: " + E.getMessage());
            log.error("runImportFromBuffer: " + E.getMessage());
        }

        return resp;

    }

}
