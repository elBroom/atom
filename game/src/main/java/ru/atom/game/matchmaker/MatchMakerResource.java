package ru.atom.game.matchmaker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.atom.game.auth.Authorized;
import ru.atom.game.dao.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ru.atom.game.model.Game;
import ru.atom.game.model.Score;
import ru.atom.game.model.Token;
import ru.atom.game.model.User;
import ru.atom.game.util.ThreadSafeQueue;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Map;

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
        String s_token = tokenParam.substring("Bearer".length()).trim();
        log.info("Join with token " + s_token);
        Transaction txn = null;
        try (Session session = Database.session()) {
            txn = session.beginTransaction();
            Token token = TokenDao.getInstance().getByToken(session, s_token);
            if (token == null) {
                log.info("Token not found");
                response = Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                ThreadSafeQueue.getInstance().offer(token.getUser());
                while (MatchMaker.getLink(token.getUser()) == null);
                response = Response.ok("localhost:8090/game/?id=" + MatchMaker.popLink(token.getUser())).build();
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

        if (jobj.get("id").isJsonNull() || jobj.get("id").toString().isEmpty() || jobj.get("result").isJsonNull() ||
                jobj.get("result").toString().isEmpty()) {
            log.info("Params empty");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Response response;
        log.info("Finish id " + jobj.get("id"));
        Transaction txn = null;
        try (Session session = Database.session()) {
            txn = session.beginTransaction();

            Game game = GameDao.getInstance().getById(session, jobj.get("id").getAsInt());
            if (game == null){
                log.info("Game " + jobj.get("id") + " not found");
                response = Response.status(Response.Status.BAD_REQUEST).build();
            } else {
                for (Map.Entry<String, JsonElement> result : jobj.get("result").getAsJsonObject().entrySet()) {
                    User user = UserDao.getInstance().getByName(session, result.getKey());
                    if (user == null){
                        log.info("User " + result.getKey() + " not found");
                        txn.rollback();
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    } else {
                        Score score = new Score().setGame(game).setUser(user).setValue(result.getValue().getAsInt());
                        ScoreDao.getInstance().insert(session, score);
                    }
                }
                response = Response.ok("ok").build();
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
}
