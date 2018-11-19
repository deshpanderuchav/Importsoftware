package com.liconic.rest.client;

import com.liconic.binding.stx.ObjectFactory;
import com.liconic.binding.stx.STXCommand;
import com.liconic.binding.stx.STXParameter;
import com.liconic.binding.stx.STXPlate;
import com.liconic.binding.stx.STXRequest;
import com.liconic.stages.ConsolidationStage;
import com.liconic.utils.XMLUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import org.apache.logging.log4j.Logger;

public class ConsolidationClient {

    private WebTarget webTarget;
    private Client client;
    private Logger log;
    private Alert alert;
    private ObjectFactory of;

    public ConsolidationClient(String url, Logger log) {

        this.log = log;

        try {
            client = ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("ConsolidationClient create Client: " + E.getMessage());
        }

        try {
            String path = "/xml/pick";
            webTarget = client.target(url).path(path);
            log.info("Consolidation REST API Endpoint: " + url + path);
        } catch (Exception E) {
            log.error("ConsolidationClient create WebTarget: " + E.getMessage());
        }

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("ConsolidationClient create ObjectFactory: " + E.getMessage());
        }
    }

    public STXRequest setConsolidation(String userId, String consolidationType, String jobId, String partition,
            String sourcePartition, String targetPartition, String tubePicker, String sourceType, String targetType, File platesFile) {

        STXRequest request = of.createSTXRequest();
        STXRequest response = null;

        try {
            log.info("run Consolidation");

            STXCommand command = of.createSTXCommand();
            command.setID(jobId);
            command.setCmd("PickTubes");
            command.setUser(userId);
            command.setPartition(partition);

            STXParameter parameter;
            STXPlate plate;

            parameter = of.createSTXParameter();
            parameter.setParameter("Consolidation");
            command.getParameters().add(parameter);

            if (ConsolidationStage.CONSOLIDATION_TYPE_REFORMATTING.equals(consolidationType)) {
                parameter = of.createSTXParameter();
                parameter.setParameter("Source Partition");
                parameter.setValue(sourcePartition);
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("Target Partition");
                parameter.setValue(targetPartition);
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("Tube Picker");
                parameter.setValue(tubePicker);
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("Source Type");
                parameter.setValue(sourceType);
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("Target Type");
                parameter.setValue(targetType);
                command.getParameters().add(parameter);
            }

            if (ConsolidationStage.CONSOLIDATION_TYPE_BY_PLATES.equals(consolidationType)) {
                FileReader file = new FileReader(platesFile);
                BufferedReader reader = new BufferedReader(file);
                String line;
                while ((line = reader.readLine()) != null) {
                    plate = of.createSTXPlate();
                    plate.setPltBCR(line);
                    request.getPlates().add(plate);
                }
            }

            request.setCommand(command);
            
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
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Consolidation created successfully!");
                log.info("runConsolidation: " + status);
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Consolidation creation failed! " + errorMsg);
                log.error("runConsolidation error msg: " + errorMsg);
            }
            alert.showAndWait();

        } catch (Exception e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Consolidation creation failed! " + e.getMessage());
            alert.showAndWait();
            System.out.println("Error: runConsolidation: " + e.getMessage());
            log.error("runConsolidation: " + e.getMessage());
        }

        return response;
    }
}
