package com.liconic.rest.client;

import com.liconic.binding.sys.ObjectFactory;
import com.liconic.binding.sys.STXCommand;
import com.liconic.binding.sys.STXParameter;
import com.liconic.binding.sys.STXRequest;
import com.liconic.stages.InventoryStage;

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

public class InventoryClient {

    private WebTarget webTarget;
    private Client client;
    private Logger log;
    private Alert alert;
    private ObjectFactory of;

    public InventoryClient(String url, Logger log) {

        this.log = log;

        try {
            client = ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("InventoryClient create Client: " + E.getMessage());
        }

        try {
            String path = "/xml/inventory";
            webTarget = client.target(url).path(path);
            log.info("Inventory REST API Endpoint: " + url + path);
        } catch (Exception E) {
            log.error("InventoryClient create WebTarget: " + E.getMessage());
        }

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("InventoryClient create ObjectFactory: " + E.getMessage());
        }
    }

    public STXRequest setInventory(String userId, String inventoryType, String jobId, String partition,
            int firstCassette, int lastCassette, String device, boolean scan, File plateFile) {

        STXRequest request = of.createSTXRequest();
        STXRequest response = null;

        try {
            log.info("run Inventory");

            STXCommand command = of.createSTXCommand();
            command.setID(jobId);
            command.setCmd("Inventory");
            command.setUser(userId);

            STXParameter parameter;

            if (InventoryStage.INVENTORY_TYPE_PARTITION.equals(inventoryType)) {
                command.setPartition(partition);
            }

            if (InventoryStage.INVENTORY_TYPE_DEFINED_CASSETTES.equals(inventoryType)) {
                parameter = of.createSTXParameter();
                parameter.setParameter("Device");
                parameter.setValue(device);
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("First Cassette");
                parameter.setValue(Integer.toString(firstCassette));
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("Last Cassette");
                parameter.setValue(Integer.toString(lastCassette));
                command.getParameters().add(parameter);
            }

            parameter = of.createSTXParameter();
            parameter.setParameter("2D Scan");
            parameter.setValue(Boolean.toString(scan));
            command.getParameters().add(parameter);
            request.setCommand(command);

            if (InventoryStage.INVENTORY_TYPE_DEFINED_PLATE.equals(inventoryType)) {
                FileReader file = new FileReader(plateFile);
                BufferedReader read = new BufferedReader(file);
                String line;
                while ((line = read.readLine()) != null) {
                    parameter = of.createSTXParameter();
                    parameter.setParameter("Plate");
                    parameter.setValue(line);
                    command.getParameters().add(parameter);
                }
            }

            response = webTarget.request().post(Entity.xml(request), STXRequest.class);

            String status = "";
            String errorMsg = "";
            if ((null != response) && (null != response.getAnswer())) {
                status = response.getAnswer().getStatus();
                errorMsg = response.getAnswer().getErrMsg();
            }

            if ("OK".equals(status)) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Inventory created successfully!");
                log.info("runInventory: " + status);
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Inventory creation failed! " + errorMsg);
                log.error("runInventory error msg: " + errorMsg);
            }
            alert.showAndWait();

        } catch (Exception e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Inventory creation failed! " + e.getMessage());
            alert.showAndWait();
            System.out.println("Error: runInventory: " + e.getMessage());
            log.error("runInventory: " + e.getMessage());
        }

        return response;
    }
}
