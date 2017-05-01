package ru.atom.game.event;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.atom.game.event.EventHandler;

@SuppressWarnings("serial")
public class EventServlet extends WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.register(EventHandler.class);
    }
}
