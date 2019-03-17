package com.liconic.rest.client;

import com.liconic.binding.conffiles.Parameter;
import com.liconic.binding.conffiles.Parameters;
import com.liconic.binding.stx.ObjectFactory;
import com.liconic.binding.stx.STXCommand;
import com.liconic.binding.stx.STXParameter;
import com.liconic.binding.stx.STXPlate;
import com.liconic.binding.stx.STXRequest;
import com.liconic.utils.XMLUtil;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.apache.logging.log4j.Logger;

public class ExportJobClient {

    private WebTarget webTarget;
    private Client client;
    private Logger log;
    private ObjectFactory of;

    public ExportJobClient(String url, Logger log) {

        this.log = log;

        try {
            client = ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("ExportJobClient create Client: " + E.getMessage());
        }

        try {
            String path = "/xml/export";
            webTarget = client.target(url).path(path);
            log.info("Export Job REST API Endpoint: " + url + path);
        } catch (Exception E) {
            log.error("ExportJobClient create WebTarget: " + E.getMessage());
        }

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("ExportClient create ObjectFactory: " + E.getMessage());
        }
    }

    public STXRequest runExportJob(String jobId, String plateBC, String userId) {

        log.info("run Export Job");

        STXRequest request = of.createSTXRequest();
        STXRequest response = null;

        STXCommand command = of.createSTXCommand();
        command.setID(Integer.toString(new Random().nextInt(100)+1)+ Character.toString((char)(new Random().nextInt(26)+'a')));
        command.setCmd("RetrievePlate");
        command.setUser(userId);
        command.setTransferstation("1");
        request.setCommand(command);
        
         STXParameter parameter = of.createSTXParameter();
         parameter.setParameter("PickJob");
         parameter.setValue(jobId);
         command.getParameters().add(parameter);
         
         parameter = of.createSTXParameter();
         parameter.setParameter("Target Partition");
         parameter.setValue("Buffer1");
         command.getParameters().add(parameter);

//        STXPlate plate = of.createSTXPlate();
//      //  plate.setPltBCR(plateBCR);
//        request.getPlates().add(plate);

        System.out.println("Request XML: \n" + XMLUtil.xmlToString(request, STXRequest.class, ObjectFactory.class));
        
        response = webTarget.request().post(Entity.xml(request), STXRequest.class);
        
        System.out.println("Response XML: \n" + XMLUtil.xmlToString(response, STXRequest.class, ObjectFactory.class));

        return response;
    }

}
