package dat.dtos;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class SkillStatsResponseDTO {
    private List<SkillStatDTO> data;
}
