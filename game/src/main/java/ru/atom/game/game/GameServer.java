package ru.atom.game.game;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.atom.game.controller.TickerStart;

public class GameServer {
    public static final String KEY = "key12345678";
    public static final Integer PORT = 8090;


    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(PORT);
        server.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { context, createResourceContext() });
        server.setHandler(contexts);

        // Add a websocket to a specific path spec
        ServletHolder holderEvents = new ServletHolder("ws-events", GameServlet.class);
        context.addServlet(holderEvents, "/events/*");
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");

        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages",
                "ru.atom.game.game"
        );

        Thread tickerStart = new Thread(new TickerStart());
        tickerStart.setName("game-server");
        tickerStart.start();

        try {
            server.start();
            server.dump(System.err);
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    private static ContextHandler createResourceContext() {
        ContextHandler context = new ContextHandler();
        context.setContextPath("/game/*");
        ResourceHandler handler = new ResourceHandler();
        handler.setWelcomeFiles(new String[]{"index.html"});

        // Хорошо бы сделать так
        // String serverRoot = GameServer.class.getResource("/front").toString();
        String serverRoot = "/home/zarina/technoatom/java/atom/bomberman/frontend/src/main/webapp";
        handler.setResourceBase(serverRoot);
        context.setHandler(handler);
        return context;
    }
}
