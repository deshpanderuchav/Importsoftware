/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.rest.client;

import com.liconic.binding.stx.STXRequest;
import com.liconic.binding.sys.ObjectFactory;
import com.liconic.binding.sys.Sys;
import com.liconic.utils.XMLUtil;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author Rucha Deshpande
 */
public class Fnclient {
        
     private WebTarget webTarget, target;
       private Client client;
  Logger log;

    private ObjectFactory of;
    
    public Fnclient(String url, Logger log) {
       this.log = log;

        try {
            client = javax.ws.rs.client.ClientBuilder.newClient();
            
        } catch (Exception E) {
            log.error("fn create Client: " + E.getMessage());
        }
        try {
           String path = "/scheduler";
            webTarget = client.target(url).path(path);
            log.info("Check continue eligibilty REST API Endpoint: " + url + path);
            
        } catch (Exception E) {
            log.error("Fn create WebTarget: " + E.getMessage());
        }
    
        try {
            of = new ObjectFactory();
        } catch (Exception E) {
            log.error("Fn create ObjectFactory: " + E.getMessage());
        }
    }
     public int superP(int idJob, String UserId) throws Exception {

        Sys sys = new Sys();
        int status;

        try {

            log.info("run SuperPriority");
            Response response =webTarget.path("jobs").path("{idJob}").resolveTemplate("idJob", idJob).path("priority").path("1").request().put(Entity.xml(""));
              status = response.getStatus();
            log.info("SuperPriority response: " + status);

        } catch (Exception E) {
            System.out.println("Error: Super Priority response: " + E.getMessage());
            log.error("Error: Super Priority response: " + E.getMessage());
            throw new Exception(E.getMessage());
        }

        return status;

    }
    
    public int checkContinue(int idtask){
        Sys sys = null;
        int status;
        try {
            log.info("Check continue eligibility");
            String id = Integer.toString(idtask);
           
            sys = webTarget.path("tasks").path("{idtask}").resolveTemplate("idtask", id).request(MediaType.APPLICATION_XML).get(Sys.class);
            
             } catch (Exception E) {
            System.out.println("Error: Continue Eligibility: " + E.getMessage());
            log.error("Error: Continue Eligibility:: " + E.getMessage());            
             }
             String action ="";
            if(sys.getJobs().getValue().getJob().get(0).getTasks().get(0).getCommands().getValue().getCmd()!=null){
            int count = sys.getJobs().getValue().getJob().get(0).getTasks().get(0).getCommands().getValue().getCmd().size();
            for(int i = 0; i < count; i++)
            {
                action = sys.getJobs().getValue().getJob().get(0).getTasks().get(0).getCommands().getValue().getCmd().get(i).getId();
                if(action.contentEquals("Continue"))
            {
                System.out.println("Return 1");
               return 1;
            }
            }
            if(!action.contentEquals("Continue"))
            {
                System.out.println("Return 0");
                return 0;
            }
        }
        else{
                System.out.println("Return 0");
          return 0;
        }

                System.out.println("Return 0");
        return 0;
    }

     public int continuePick(int idtask) throws Exception {
        int status;
        Sys sys = new Sys();
         Entity<?> empty = Entity.text("");
        try {
            log.info("Continue task: " + idtask);
            String id = Integer.toString(idtask);
          //  Response response =webTarget.path("tasks").path("{idtask}").resolveTemplate("idtask", idtask).path("action").path("continue").request().put(empty, Response.class);
            Response response = webTarget.path(java.text.MessageFormat.format("tasks/{0}/action/continue", new Object[]{id})).request().put(empty, Response.class);
            System.out.println(response.getLocation());
            status = response.getStatus();
            System.out.println("Continue pick response: " + status);
            log.info("Continue pick response: " + status);

        } catch (Exception E) {
            System.out.println("Error: Continue pick response: " + E.getMessage());
            log.error("Error: Continue pick response: " + E.getMessage());
            throw new Exception(E.getMessage());
        }
        return status;
    }
}
