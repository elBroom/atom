package ru.atom.game.game;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.atom.game.matchmaker.MatchMaker;
import ru.atom.game.network.GameSessionPool;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by zarina on 13.05.17.
 */
@Path("/")
public class GameResource {
    private static final Logger log = LogManager.getLogger(GameResource.class);

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/createGame")
    public Response finish(String key) {
        if (GameServer.KEY.equals(key)) {
            Gson gson = new Gson();
            LinkedList<String> links = new LinkedList<>();
            GameSession gameSession = new GameSession();
            log.info("Created game session");
            for (int i = 0; i < MatchMaker.PLAYERS_IN_GAME; i++) {
                String link = UUID.randomUUID().toString();
                links.add(link);
                GameSessionPool.getInstance().add(link, gameSession);
            }
            log.info("Created four links");

            return Response.ok(gson.toJson(links)).build();
        } else {
            log.info("Key not valid");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
