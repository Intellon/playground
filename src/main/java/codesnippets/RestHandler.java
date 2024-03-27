package codesnippets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class RestHandler {
    public static void main(String[] args) throws IOException {

        URL weburl = new URL("https://dummyjson.com/products");
        Proxy webProxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.essd.ch", 3123));
        HttpURLConnection webProxyConnection = (HttpURLConnection) weburl.openConnection(webProxy);
        webProxyConnection.setRequestProperty("Content-Type", "application/json");
        webProxyConnection.setDoOutput(true);
        webProxyConnection.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(webProxyConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}
