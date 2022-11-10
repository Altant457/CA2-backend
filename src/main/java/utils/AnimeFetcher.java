package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dtos.AnimeDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimeFetcher {
    public static List<AnimeDTO> getMultiData(String query) throws IOException {
        List<AnimeDTO> animeDTOs = new ArrayList<>();
        String res = HttpUtils.fetchAnime(query, true);
        JsonObject json = JsonParser.parseString(res).getAsJsonObject();
        for (int i = 0; i <= 19; i++) {
            String id = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("id").getAsString();
            String name = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("canonicalTitle").getAsString();
            String year = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("startDate").getAsString().substring(0,4);
            String status = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("status").getAsString();
            animeDTOs.add(new AnimeDTO(id, name, year, status));
        }
        return animeDTOs;
    }

    public static AnimeDTO getSingleData(String sname) throws IOException {
        String res = HttpUtils.fetchAnime(sname, false);
        JsonObject json = JsonParser.parseString(res).getAsJsonObject();
        String id = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("id").getAsString();
        String name = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("canonicalTitle").getAsString();
        String year = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("startDate").getAsString().substring(0,4);
        String status = json.get("data").getAsJsonArray().get(0).getAsJsonObject()
                .get("attributes").getAsJsonObject()
                .get("status").getAsString();
        return new AnimeDTO(id, name, year, status);
    }
}
