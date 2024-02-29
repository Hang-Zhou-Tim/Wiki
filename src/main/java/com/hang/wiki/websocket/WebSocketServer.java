package com.hang.wiki.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;

@Component
@ServerEndpoint("/ws/{token}")
public class WebSocketServer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * Each client will have one token
     */
    private String token = "";

    private static HashMap<String, Session> map = new HashMap<>();

    /**
     * Connection Succeed
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        map.put(token, session);
        this.token = token;
        LOG.info("New Connection: token：{}，session id：{}，Current Connection Number: {}", token, session.getId(), map.size());
    }

    /**
     * Close Conneciton
     */
    @OnClose
    public void onClose(Session session) {
        map.remove(this.token);
        LOG.info("Connection Closed, token：{}，session id：{}！Current Connection Number:{}", this.token, session.getId(), map.size());
    }

    /**
     * Get Message
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("Receive Message, Token: {}, Content: {}", token, message);
    }

    /**
     * Connection Failed
     */
    @OnError
    public void onError(Session session, Throwable error) {
        LOG.error("Connection Error", error);
    }

    /**
     * Broadcast Message
     */
    public void sendInfo(String message) {
        for (String token : map.keySet()) {
            Session session = map.get(token);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                LOG.error("Failed to Broadcast Message: {}, Content: {}", token, message);
            }
            LOG.info("Sent Message: {}，Content: {}", token, message);
        }
    }
}
