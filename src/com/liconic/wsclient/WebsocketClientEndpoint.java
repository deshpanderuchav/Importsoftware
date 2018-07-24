package com.liconic.wsclient;

import com.liconic.stages.ImportStage;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.apache.logging.log4j.Logger;

@ClientEndpoint
public class WebsocketClientEndpoint {

    Session userSession = null;
    private MessageHandler messageHandler;
    private ImportStage importStage;
    private Logger log;

    public WebsocketClientEndpoint(ImportStage importStage, URI endpointURI, Logger log) {

        this.importStage = importStage;
        this.log = log;

        try {

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);

        } catch (Exception e) {
            log.error("Create WebSocketContainer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("opening websocket");
        log.info("Opening websocket");
        this.userSession = userSession;
        this.userSession.setMaxTextMessageBufferSize(100000);
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        log.info("Closing websocket");
        this.userSession = null;
        importStage.setDisconnectWS();
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Websocket error: " + throwable.getMessage());

    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {

        public void handleMessage(String message);
    }

    public void Close() {

        try {
            userSession.close();
        } catch (Exception E) {
            log.error("Closing websocket: " + E.getMessage());
            System.out.println(E.getMessage());
        }

    }
}
