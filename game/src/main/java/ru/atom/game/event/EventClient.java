package ru.atom.game.event;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import ru.atom.game.message.Topic;
import ru.atom.game.message.Message;
import ru.atom.game.util.JsonHelper;

import java.net.URI;
import java.util.concurrent.Future;

public class EventClient {
    public static void main(String[] args) {
        URI uri = URI.create("ws://wtfis.ru:8090/events/");//CHANGE TO wtfis.ru for task

        WebSocketClient client = new WebSocketClient();
        //client.setMasker(new ZeroMasker());
        try {
            try {
                client.start();
                // The socket that receives events
                EventHandler socket = new EventHandler();
                // Attempt Connect
                Future<Session> fut = client.connect(socket, uri);
                // Wait for Connect
                Session session = fut.get();
                // Send a message
                Message message = new Message(Topic.HELLO, "Hitler");
                //TODO TASK: implement sending Message with type HELLO and your name as
                session.getRemote().sendString(JsonHelper.toJson(message));
                // Close session
                session.close();
            } finally {
                client.stop();
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
