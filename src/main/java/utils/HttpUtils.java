package utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HttpUtils {

    public static String fetchData(String _url) throws MalformedURLException, IOException, FileNotFoundException {
        URL url = new URL(_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        //con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("User-Agent", "server");

        Scanner scan = new Scanner(con.getInputStream());
        StringBuilder jsonStr = new StringBuilder();
        while (scan.hasNext()) {
            jsonStr.append(scan.nextLine());
        }
        scan.close();
        return jsonStr.toString();
    }

    public static String fetchAnime(String filter, boolean multi) throws IOException {
        String url = multi ? "https://kitsu.io/api/edge/anime/?page%5Blimit%5D=20&filter%5Btext%5D="
                : "https://kitsu.io/api/edge/anime/?page%5Blimit%5D=1&filter%5Btext%5D=";
        Client client = ClientBuilder.newClient();
        String encodedFilter = URLEncoder.encode(filter, StandardCharsets.UTF_8.toString());
        System.out.println("Request sent to: "+url+encodedFilter);
        Response response = client.target(url+encodedFilter).request().get();
        return response.readEntity(String.class);
    }
}
