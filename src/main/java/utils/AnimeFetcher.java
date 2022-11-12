package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.AnimeDTO;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnimeFetcher {
    public static List<AnimeDTO> getMultiData(String query) throws IOException {
        List<AnimeDTO> animeDTOs = new ArrayList<>();
        String res = HttpUtils.fetchAnime(query, true);
        JsonObject json = JsonParser.parseString(res).getAsJsonObject();
        if (json.get("meta").getAsJsonObject().get("count").getAsInt() == 0) {
            throw new WebApplicationException("No anime found", 404);
        }
        for (int i = 0; i <= json.get("data").getAsJsonArray().size() - 1; i++) {
            Integer id = Integer.parseInt(json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("id").getAsString());
            String name = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("canonicalTitle").getAsString();
            String startDate = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("startDate").getAsString();
            String status = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("status").getAsString();
            String endDate = (Objects.equals(status, "current")) ? "" :
                    json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                            .get("attributes").getAsJsonObject()
                            .get("endDate").getAsString();
            String posterURL = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                            .get("attributes").getAsJsonObject()
                            .get("posterImage").getAsJsonObject()
                            .get("original").getAsString();
            JsonElement synopsisObj = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                            .get("attributes").getAsJsonObject()
                            .get("synopsis");
            String synopsis = (synopsisObj != null && !synopsisObj.isJsonNull()) ? synopsisObj.getAsString() : "No synopsis available";
            animeDTOs.add(new AnimeDTO(id, name, startDate, endDate, status, posterURL, synopsis));
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
        String startDate = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("startDate").getAsString();
        String status = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("status").getAsString();
        String endDate = (Objects.equals(status, "current")) ? "" :
                json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                        .get("attributes").getAsJsonObject()
                        .get("endDate").getAsString();
        String posterURL = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("posterImage").getAsJsonObject()
                .get("original").getAsString();
        String synopsis = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("synopsis").getAsString();
        return new AnimeDTO(id, name, startDate, endDate, status, posterURL, synopsis);
    }
}
