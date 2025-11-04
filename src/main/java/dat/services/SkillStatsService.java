package dat.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dat.dtos.SkillStatsResponseDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SkillStatsService {

    private static SkillStatsService instance;
    private static final String API_URL = "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=";
    private final HttpClient client;
    private final ObjectMapper mapper;

    private SkillStatsService() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public static SkillStatsService getInstance() {
        if (instance == null) {
            instance = new SkillStatsService();
        }
        return instance;
    }

    public SkillStatsResponseDTO getSkillStats(List<String> slugs) throws IOException, InterruptedException {
        String joined = String.join(",", slugs);
        String url = API_URL + joined;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Requesting: " + url);
        System.out.println("Status code: " + response.statusCode());
        System.out.println("Response body: " + response.body());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch data from external API. Status code: " + response.statusCode());
        }

        return mapper.readValue(response.body(), SkillStatsResponseDTO.class);
    }
}
