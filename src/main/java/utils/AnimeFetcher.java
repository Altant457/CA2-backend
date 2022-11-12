package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.AnimeDTO;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimeFetcher {
    public static List<AnimeDTO> getMultiData(String query) throws IOException {
        List<AnimeDTO> animeDTOs = new ArrayList<>();
        String res = HttpUtils.fetchAnime(query, true);
        JsonObject json = JsonParser.parseString(res).getAsJsonObject();
        if (json.get("meta").getAsJsonObject().get("count").getAsInt() == 0) {
            throw new WebApplicationException("No anime found", 404);
        }
        for (int i = 0; i <= 19; i++) {
            Integer id = Integer.parseInt(json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("id").getAsString());
            String name = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("canonicalTitle").getAsString();
            String year = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("startDate").getAsString().substring(0,4);
            String status = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("status").getAsString();
            String posterURL = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                            .get("attributes").getAsJsonObject()
                            .get("posterImage").getAsJsonObject()
                            .get("large").getAsString();
            String synopsis = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                            .get("attributes").getAsJsonObject()
                            .get("synopsis").getAsString();
            animeDTOs.add(new AnimeDTO(id, name, year, status, posterURL, synopsis));
        }
        return animeDTOs;
    }

    public static AnimeDTO getSingleData(String sname) throws IOException {
        String res = HttpUtils.fetchAnime(sname, false);
        JsonObject json = JsonParser.parseString(res).getAsJsonObject();
        Integer id = Integer.parseInt(json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("id").getAsString());
        String name = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("canonicalTitle").getAsString();
        String year = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("startDate").getAsString().substring(0,4);
        String status = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("status").getAsString();
        String posterURL = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("posterImage").getAsJsonObject()
                .get("original").getAsString();
        String synopsis = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("synopsis").getAsString();
        return new AnimeDTO(id, name, year, status, posterURL, synopsis);
    }
}
