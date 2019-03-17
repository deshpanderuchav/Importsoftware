/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.rest.client;

import com.liconic.binding.stx.ObjectFactory;
import com.liconic.binding.stx.STXCommand;
import com.liconic.binding.stx.STXParameter;
import com.liconic.binding.stx.STXPlate;
import com.liconic.binding.stx.STXRequest;
import com.liconic.binding.sys.Plate;
import com.liconic.binding.sys.STXTubePos;
import com.liconic.utils.XMLUtil;
import java.math.BigInteger;
import javafx.scene.control.Alert;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rucha Deshpande
 */
public class ImportJobClient {
    
    private WebTarget webTarget;
    private Client client;
    private Logger log;
    private Alert alert;
    private ObjectFactory of;
 String PRELOAD = "PRELOAD";
    
    public ImportJobClient(String url, Logger log) {

        this.log = log;

        
        try {
            client = ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("ImportJobClient create Client: " + E.getMessage());
        }

        try {
            String path = "/xml/import";
            webTarget = client.target(url).path(path);
            log.info("ImportJobClient REST API Endpoint: " + url + path);
        } catch (Exception E) {
            log.error("ImportJobClient create WebTarget: " + E.getMessage());
        }

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("ImportJobClient create ObjectFactory: " + E.getMessage());
        }
    }
    
    public void setImport(String user, String importType,String jobId,String partition,String plateBarcode, boolean empty, String type){
        
        STXRequest request = of.createSTXRequest();
        STXRequest response = null;

        try{
            log.info("run Import: ");

            STXCommand command = of.createSTXCommand();
            command.setID(jobId);
            command.setCmd("StorePlate");
            command.setUser(user);
            command.setPartition(partition);
            command.setTransferstation("1");
            
            STXParameter parameter;
            parameter = of.createSTXParameter();
            
            if(empty != true){
            parameter.setParameter("2D Scan");
            command.getParameters().add(parameter);
            }
            
             parameter = of.createSTXParameter();
            
            if(PRELOAD.equals(importType)){
            parameter.setParameter("Preload");
            command.getParameters().add(parameter);
            }
            
            
            request.setCommand(command);
            
            STXPlate plate = of.createSTXPlate();
            plate.setPltBCR(plateBarcode);
            plate.setPltMat("Serum");
            
            
            com.liconic.binding.stx.STXTubePos tpos = of.createSTXTubePos();
            
            tpos.setPX((byte)1);
            tpos.setPY((byte)1);
            tpos.setPYA("A");
            
            if(type.contains("0.3ml")){
               tpos.setPTV(BigInteger.valueOf(1));
            plate.setPltType(BigInteger.valueOf(1));
            }
            else if(type.equals("Nunc 1.8ml 48")){
                tpos.setPTV(BigInteger.valueOf(2));
                plate.setPltType(BigInteger.valueOf(2));
            }
            else if(type.equals("Nunc 1.8ml 60")){
                tpos.setPTV(BigInteger.valueOf(3));
            plate.setPltType(BigInteger.valueOf(3));
            }
            plate.getPltTPos().add(tpos);
            
            request.getPlates().add(plate);
            
            System.out.println("Request XML: \n" + XMLUtil.xmlToString(request, STXRequest.class, ObjectFactory.class));

            response = webTarget.request().post(Entity.xml(request), STXRequest.class);

            System.out.println("Response XML: \n" + XMLUtil.xmlToString(response, STXRequest.class, ObjectFactory.class));
            
            String status = "";
            String errorMsg = "";
            if ((null != response) && (null != response.getAnswer())) {
                status = response.getAnswer().getStatus();
                errorMsg = response.getAnswer().getErrMsg();
            }

            if ("OK".equals(status)) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Import job created successfully!");
                log.info("Import job: " + status);
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Import plate creation failed! " + errorMsg);
                log.error("Import job error msg: " + errorMsg);
            }
            alert.showAndWait();

        } catch (Exception e) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Import plate creation failed! " + e.getMessage());
            alert.showAndWait();
            System.out.println("Error: Import job error msg: " + e.getMessage());
            log.error("Import job error msg:: " + e.getMessage());
        }

    }
}
