package dat.dtos;

import dat.services.SkillStatsService.SkillStatDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateMarketDataDTO {
    private int id;
    private String name;
    private String education;
    private String phone;
    private List<SkillStatDTO> skills;
}
