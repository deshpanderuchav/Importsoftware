package com.liconic.rest.client;

import com.liconic.binding.stx.ObjectFactory;
import com.liconic.binding.stx.STXCommand;
import com.liconic.binding.stx.STXParameter;
import com.liconic.binding.stx.STXPlate;
import com.liconic.binding.stx.STXRequest;
import com.liconic.binding.stx.STXTubePos;
import com.liconic.stages.PickJobStage;
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

public class PickJobClient {

    private WebTarget webTarget;
    private Client client;
    private Logger log;
    private Alert alert;
    private ObjectFactory of;

    public PickJobClient(String url, Logger log) {

        this.log = log;

        try {
            client = ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("PickJobClient create Client: " + E.getMessage());
        }

        try {
            String path = "/xml/pick";
            webTarget = client.target(url).path(path);
            log.info("Pick Job REST API Endpoint: " + url + path);
        } catch (Exception E) {
            log.error("PickJobClient create WebTarget: " + E.getMessage());
        }

        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("PickJobClient create ObjectFactory: " + E.getMessage());
        }
    }

    public STXRequest setPickJob(String userId, String pickJobType, String jobId, String partition,
            String targetPartition, String tubePicker, String targetType, File samplesFile) {

        STXRequest request = of.createSTXRequest();
        STXRequest response = null;

        try {
            log.info("run Pick Job");

            STXCommand command = of.createSTXCommand();
            command.setID(jobId);
            command.setCmd("PickTubes");
            command.setUser(userId);

            STXParameter parameter;
            STXRequest.Tubes tube;
            STXPlate plate;
            STXTubePos tubePos;

            if (PickJobStage.PICK_JOB_TYPE_WITH_CUSTOM_TUBE_PICKER.equals(pickJobType)) {
                command.setPartition(partition);

                parameter = of.createSTXParameter();
                parameter.setParameter("Target Partition");
                parameter.setValue(targetPartition);
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("Tube Picker");
                parameter.setValue(tubePicker);
                command.getParameters().add(parameter);

                parameter = of.createSTXParameter();
                parameter.setParameter("Target Type");
                parameter.setValue(targetType);
                command.getParameters().add(parameter);
            }

            request.setCommand(command);

            FileReader file = new FileReader(samplesFile);
            BufferedReader reader = new BufferedReader(file);
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                // Tube
                tube = of.createSTXRequestTubes();
                tube.setTube(tokens[0]);
                // Plate
                if (tokens.length == 2 || tokens.length == 4) {
                    plate = of.createSTXPlate();
                    plate.setPltBCR(tokens[1]);
                    tube.setPlate(plate);
                }
                // Plate Position
               if (tokens.length == 4) {
                    tubePos = of.createSTXTubePos();
                    tubePos.setPX(Byte.valueOf(tokens[2]));
                    Byte py = Byte.valueOf(tokens[3]);
                    tubePos.setPY(py);
                    tubePos.setPYA(Character.toString((char)(py + 'A' - 1)));
                    tube.setPltTPos(tubePos);
                }
                request.getTubes().add(tube);
            }
            
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
                alert.setHeaderText("Pick Job created successfully!");
                log.info("runPickJob: " + status);
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Pick Job creation failed! " + errorMsg);
                log.error("runPickJob error msg: " + errorMsg);
            }
            alert.showAndWait();

        } catch (Exception e) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Pick Job creation failed! " + e.getMessage());
            alert.showAndWait();
            System.out.println("Error: runPickJob: " + e.getMessage());
            log.error("runPickJob: " + e.getMessage());
        }

        return response;
    }
}
