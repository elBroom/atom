package ru.atom.game.matchmaker;

import javafx.util.Pair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.atom.game.dao.Database;

import java.util.concurrent.ConcurrentHashMap;


public class MatchMakerServer {
    private static ConcurrentHashMap<String, Pair<String, String>> gameServers = new ConcurrentHashMap<>();

    public static void init() {
        gameServers.put("gs0", new Pair<>("key12345678", "localhost:8090"));
    }

    public static Pair<String, String> getGameServer() {
        // TODO Balance for gameServers
        return gameServers.get("gs0");
    }

    public static void main(String[] args) throws Exception {
        init();
        Database.setUp();
        ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/mm/*");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.packages",
                "ru.atom.game.matchmaker"
        );

        jerseyServlet.setInitParameter(
                "com.sun.jersey.spi.container.ContainerResponseFilters",
                CrossBrowserFilter.class.getCanonicalName()
        );

        jettyServer.start();

        Thread matchMaker = new Thread(new MatchMaker());
        matchMaker.setName("match-maker");
        matchMaker.start();
    }
}

