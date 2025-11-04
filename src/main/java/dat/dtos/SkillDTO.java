package dat.dtos;

import dat.entities.Skill;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO {
    private int id;
    private String name;
    private String slug;
    private Integer popularityScore;
    private Integer averageSalary;

    public SkillDTO(Skill skill) {
        this.id = skill.getId();
        this.name = skill.getName();
        this.slug = skill.getSlug();
        this.popularityScore = skill.getPopularityScore();
        this.averageSalary = skill.getAverageSalary();
    }
}
