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
            String status = json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                    .get("attributes").getAsJsonObject()
                    .get("status").getAsString();
            String startDate = (Objects.equals(status, "tba")) ? "" :
                    json.get("data").getAsJsonArray().get(i).getAsJsonObject()
                            .get("attributes").getAsJsonObject()
                            .get("startDate").getAsString();
            String endDate = (Objects.equals(status, "current")) || Objects.equals(status, "tba") ? "" :
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
            if (synopsis.equals("")) {
                synopsis = "No synopsis available";
            }
            animeDTOs.add(new AnimeDTO(id, name, startDate, endDate, status, posterURL, synopsis));
        }
        return animeDTOs;
    }
}
