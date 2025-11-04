package dat.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dat.exceptions.ApiException;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

public class SkillStatsService {

    private final FetchTools fetchTools;

    public SkillStatsService(FetchTools fetchTools) {
        this.fetchTools = fetchTools;
    }

    public ResponseDTO getSkillStats(List<String> slugs) {
        if (slugs == null || slugs.isEmpty()) {
            return new ResponseDTO(List.of());
        }

        String uri = buildUri(slugs);
        ResponseDTO response = fetchTools.getFromApi(uri, ResponseDTO.class);

        if (response == null) {
            throw new ApiException(500, "Failed to fetch skill statistics from external API");
        }

        return response;
    }

    private static String buildUri(List<String> slugs) {
        String joined = String.join(",", slugs);
        return "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" + joined;
    }

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ResponseDTO {
        private List<SkillStatDTO> data;
    }

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SkillStatDTO {
        private String id;
        private String slug;
        private String name;
        private String category;
        private String description;
        private Integer popularityScore;
        private Integer averageSalary;
        private ZonedDateTime updatedAt;
    }
}
