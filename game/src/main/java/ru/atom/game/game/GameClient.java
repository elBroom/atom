package ru.atom.game.game;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GameClient {
    private static final Logger log = LogManager.getLogger(GameClient.class);

    public static LinkedList<String> createGameSession(String address, String key) throws IOException {
        log.info("Build request to gs by address: {}", "http://" + address + "/createGame");
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody requestBody = RequestBody.create(mediaType, key);
        Request request = new Request.Builder().post(requestBody)
                .url("http://" + address + "/createGame").build();
        Response response = new OkHttpClient().newCall(request).execute();
        
        log.info("parse response");
        Gson gson = new Gson();
        LinkedList<String> links = new LinkedList<>();
        for (Object link: gson.fromJson(response.body().string(), List.class)) {
            links.add((String) link);
        }
        return links;
    }
}
