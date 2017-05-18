package ru.atom.game.matchmaker;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.atom.game.auth.Authorized;
import ru.atom.game.dao.Database;
import ru.atom.game.dao.GameDao;
import ru.atom.game.dao.TokenDao;
import ru.atom.game.model.Game;
import ru.atom.game.model.Token;
import ru.atom.game.util.ThreadSafeQueueUser;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zarina on 01.05.17.
 */
@Path("/")
public class MatchMakerResource {
    private static final Logger log = LogManager.getLogger(MatchMakerResource.class);

    @Authorized
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/join")
    public Response join(@HeaderParam(HttpHeaders.AUTHORIZATION) String tokenParam) {
        Response response;
        String receivedToken = tokenParam.substring("Bearer".length()).trim();
        log.info("Join with token " + receivedToken);
        Transaction txn = null;
        try (Session session = Database.session()) {
            txn = session.beginTransaction();
            Token token = TokenDao.getInstance().getByToken(session, receivedToken);
            if (token == null) {
                log.info("Token not found");
                response = Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                ThreadSafeQueueUser.getInstance().offer(token.getUser());
                Optional<Pair<String, String>> link = MatchMaker.tryPopLink(token.getUser(), 1000);
                if (link.isPresent()) {
                    Game game = new Game().setSublink(link.get().getValue()).setUser(token.getUser());
                    GameDao.getInstance().insert(session, game);
                    response = Response
                            .ok("http://" + link.get().getKey() + "/game/?id=" + link.get().getValue()).build();
                } else {
                    response = Response.status(Response.Status.GATEWAY_TIMEOUT).build();
                }
            }

            txn.commit();
        } catch (RuntimeException e) {
            log.error("Transaction failed.", e);
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            response = Response.status(Response.Status.BAD_GATEWAY).build();
        }
        return response;
    }

    @POST
    @Consumes("application/json")
    @Path("/finish")
    public Response finish(String json) {
        JsonObject jobj = new Gson().fromJson(json, JsonObject.class);

        if (jobj.get("id").isJsonNull() || jobj.get("id").toString().isEmpty() || jobj.get("result").isJsonNull()
                || jobj.get("result").toString().isEmpty()) {
            log.info("Params empty");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Response response;
        log.info("Finish id " + jobj.get("id"));
        Transaction txn = null;
        try (Session session = Database.session()) {
            txn = session.beginTransaction();
            for (Map.Entry<String, JsonElement> result : jobj.get("result").getAsJsonObject().entrySet()) {
                Game game = GameDao.getInstance().getBySublink(session, result.getKey());
                if (game == null) {
                    log.info("Game with sublink  " + result.getKey() + " not found");
                    txn.rollback();
                    return Response.status(Response.Status.BAD_REQUEST).build();
                } else {
                    game.setScore(result.getValue().getAsInt());
                    GameDao.getInstance().update(session, game);
                }
            }
            response = Response.ok("ok").build();
            txn.commit();
        } catch (RuntimeException e) {
            log.error("Transaction failed.", e);
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            response = Response.status(Response.Status.BAD_GATEWAY).build();
        }
        return response;
    }
}
