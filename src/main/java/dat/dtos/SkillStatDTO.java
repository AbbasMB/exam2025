package dat.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillStatDTO {
    private String id;
    private String slug;
    private String name;
    private String categoryKey;
    private String description;
    private Integer popularityScore;
    private Integer averageSalary;
    private String updatedAt;
}
