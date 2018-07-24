package com.liconic.rest.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;

public class SchedulerClient {

    private WebTarget webTarget;
    private Client client;
    private Logger log;
    
    public SchedulerClient(String url, Logger log) {

        this.log = log;

        try {
            client = javax.ws.rs.client.ClientBuilder.newClient();
        } catch (Exception E) {
            log.error("ImportClient create Client: " + E.getMessage());
        }

        try {
            webTarget = client.target(url);
        } catch (Exception E) {
            log.error("ImportClient create WebTarget: " + E.getMessage());
        }

    }    
        
    public void setSequence(int jobId, int sequence) throws Exception{
        
        try{
  
            Entity<?> empty = Entity.text("");

            Response responce = webTarget.path("scheduler").path(java.text.MessageFormat.format("jobs/{0}/sequence/{1}", new Object[]{String.valueOf(jobId), sequence})).request().put(empty, Response.class);

            if (responce.getStatus() != 200){
                
                throw new Exception("Error if set sequence");
             
            }
            
            log.info("Set sequence JobId="+jobId+", sequence="+sequence+", OK");
            
            
        }catch(Exception E){
            log.error(E.getMessage());
            throw E;
        }
        
    }
    
    
            
    
}
