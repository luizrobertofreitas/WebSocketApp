/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.websocket.socket;

import com.websocket.model.Device;
import java.io.StringReader;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.jboss.logging.Logger;

/**
 *
 * @author luizjunior
 */
@ApplicationScoped
@ServerEndpoint("/actions")
public class DeviceWebSocketServer {
    
    @Inject
    private DeviceSessionHandler sessionHandler;
    
    @OnOpen
    public void open(Session session){
        System.out.println("Open session: " + session.getId());
        sessionHandler.addSession(session);
    }
    
    @OnClose
    public void close(Session session) {
        System.out.println("Close session: " + session.getId());
        sessionHandler.removeSession(session);
    }
    
    @OnError
    public void onError(Throwable error) {
        System.out.println("Error got: " + error.getMessage());
        Logger.getLogger(DeviceWebSocketServer.class.getName()).log(Logger.Level.FATAL, error);
    }
    
    @OnMessage
    public void handleMessage(String message, Session session) {
        
        System.out.println("Session: " + session.getId());
        System.out.println("Message:");
        System.out.println(message);
        System.out.println("\n");
        
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("add".equals(jsonMessage.getString("action"))) {
                Device device = new Device();
                device.setName(jsonMessage.getString("name"));
                device.setDescription(jsonMessage.getString("description"));
                device.setType(jsonMessage.getString("type"));
                device.setStatus("Off");
                sessionHandler.addDevice(device);
            }

            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeDevice(id);
            }

            if ("toggle".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.toggleDevice(id);
            }
        }
    }
    
}
