package com.workeache.precionline.api.demo.utils.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GotifyClientService {

    @Value("${precionline.notifications.gotify.url}")
    private String GOTIFY_URL;

    @Value("${precionline.notifications.gotify.token}")
    private String TOKEN;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GotifyClientService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public boolean sendMessage(Message message) throws IOException, InterruptedException {
        final var bodyData = objectMapper.writeValueAsString(message);

        final var request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s/message?token=%s", GOTIFY_URL, TOKEN)))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyData))
                .build();

        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(response.body());

        return response.statusCode() >= 200 && response.statusCode() < 400;
    }
}
