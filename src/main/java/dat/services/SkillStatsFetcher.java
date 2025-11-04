package dat.services;

import java.util.List;

public class SkillStatsFetcher {
    private final FetchTools fetchTools;

    public SkillStatsFetcher(FetchTools fetchTools) {
        this.fetchTools = fetchTools;
    }

    public SkillStatsResponse fetchSkillStats(List<String> slugs) {
        String slugParam = String.join(",", slugs);
        String uri = buildUri(slugParam);
        return fetchTools.getFromApi(uri, SkillStatsResponse.class);
    }

    private static String buildUri(String slugParam) {
        return "https://apiprovider.cphbusinessapps.dk/api/v1/skills/stats?slugs=" + slugParam;
    }
}
