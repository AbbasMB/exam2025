package dat.services;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillStatsResponse {
    private List<SkillStat> data;
}
