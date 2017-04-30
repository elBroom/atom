package ru.atom.lecture08.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import ru.atom.lecture08.websocket.message.Message;
import ru.atom.lecture08.websocket.message.Topic;
import ru.atom.lecture08.websocket.network.Broker;

public class EventHandler extends WebSocketAdapter {
    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message: " + message);
        Broker.getInstance().receive(getSession(), message);
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
