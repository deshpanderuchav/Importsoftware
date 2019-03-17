package com.liconic.rest.client;

import com.liconic.binding.stx.ObjectFactory;
import com.liconic.binding.stx.STXCommand;
import com.liconic.binding.stx.STXPlate;
import com.liconic.binding.stx.STXRequest;
import com.liconic.binding.sys.Sys;
import com.liconic.db.DM;
import com.liconic.utils.XMLUtil;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;

public class ExportRackClient {

    private WebTarget webTarget;
    private Client client;
    private Logger log;

    private ObjectFactory of;

    public ExportRackClient(String url, Logger log) {
        this.log = log;

        try {
            client = ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("ExportRackClient create Client: " + E.getMessage());
        }

        try {
            webTarget = client.target(url);
        } catch (Exception E) {
            log.error("ExportRackClient create WebTarget: " + E.getMessage());
        }

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("ExportRackClient create ObjectFactory: " + E.getMessage());
        }
    }

    public STXRequest runExportRack(int idTask, int idRack, String UserId, int idPick) {
        
        STXRequest request = of.createSTXRequest();
        STXRequest response = null;

        STXCommand command = of.createSTXCommand();
        command.setCmd("RetrievePlate");
        command.setUser(UserId);
        command.setID(Integer.toString(idPick));
        command.setTransferstation("1");
        request.setCommand(command);

//        STXPlate plate = of.createSTXPlate();
//        plate.setPltBCR(plateBC);
//        request.getPlates().add(plate);

        System.out.println("Request XML: \n" + XMLUtil.xmlToString(request, STXRequest.class, com.liconic.binding.stx.ObjectFactory.class));
        
        response = webTarget.path("/xml/export").request().post(Entity.xml(request), STXRequest.class);
        
        System.out.println("Response XML: \n" + XMLUtil.xmlToString(response, STXRequest.class, com.liconic.binding.stx.ObjectFactory.class));

        return response;
        
    }
    
    public STXRequest runExportRackBC(String idJob, String plateBC,String UserId) {
        
        STXRequest request = of.createSTXRequest();
        STXRequest response = null;

        STXCommand command = of.createSTXCommand();
        command.setCmd("RetrievePlate");
        command.setUser(UserId);
        command.setID(idJob);
        command.setTransferstation("1");
        request.setCommand(command);

        STXPlate plate = of.createSTXPlate();
        plate.setPltBCR(plateBC);
        request.getPlates().add(plate);

        System.out.println("Request XML: \n" + XMLUtil.xmlToString(request, STXRequest.class, com.liconic.binding.stx.ObjectFactory.class));
        
        response = webTarget.path("/xml/export").request().post(Entity.xml(request), STXRequest.class);
        
        System.out.println("Response XML: \n" + XMLUtil.xmlToString(response, STXRequest.class, com.liconic.binding.stx.ObjectFactory.class));

        return response;
        
    }
    
    public int canceExportRack(int idTask, String UserId) throws Exception {
       
         STXRequest request = of.createSTXRequest();
        Response response = null;
        
         System.out.println("Request XML: \n" + XMLUtil.xmlToString(request, STXRequest.class, com.liconic.binding.stx.ObjectFactory.class));
        
         Entity<?> empty = Entity.text("");

         response = webTarget.path("/scheduler/tasks/"+idTask+"/action/cancel").request().put(empty,Response.class);

        System.out.println("Response XML: \n" + XMLUtil.xmlToString(response, STXRequest.class, com.liconic.binding.stx.ObjectFactory.class));

        
        /* Sys sys = new Sys();
        Sys resp = null;

        try {
            log.info("run CancelExportRack");

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
            log.info("CancelExportRack: " + resp.getSysName().getValue());

        } catch (Exception E) {
            System.out.println("Error: CancelExportRack: " + E.getMessage());
            log.error("Error: CancelExportRack: " + E.getMessage());
            throw new Exception(E.getMessage());
        }
*/
        return response.getStatus();
    }
    
    public int deleteJob(int idJob, String UserId){
     
               int status =0;
     
         try{
  
            Entity<?> empty = Entity.text("");

          //  Response response = webTarget.path("scheduler").path(java.text.MessageFormat.format("jobs/{0}", new Object[]{String.valueOf(idJob)})).request().delete(empty, Response.class);

            Response response = webTarget.path("/scheduler/jobs/"+idJob).request().delete(Response.class);
            status = response.getStatus();
    
           
         }
         catch(Exception e){
         }
        return status;
}

}