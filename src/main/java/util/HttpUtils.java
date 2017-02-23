package util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;


public class HttpUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private static HttpsURLConnection openConnection(String route) throws IOException {
        URL url = new URL(route);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        return con;
    }

    public static JsonNode get(String route) throws IOException, HttpNoSuccessException {
        HttpsURLConnection connection = openConnection(route);
        int responseCode = connection.getResponseCode();

        JsonNode response = objectMapper.readTree(connection.getInputStream());

        if (responseCode != 200) {
            throw new HttpNoSuccessException("Http call failed", responseCode);
        } else {
            return response;
        }
    }
}