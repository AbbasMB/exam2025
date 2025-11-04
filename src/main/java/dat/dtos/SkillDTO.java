package dat.dtos;

import dat.entities.Skill;
import dat.enums.SkillCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDTO {
    private int id;
    private String name;
    private SkillCategory category;
    private String description;

    public SkillDTO(Skill s) {
        this.id = s.getId();
        this.name = s.getName();
        this.category = s.getCategory();
        this.description = s.getDescription();
    }

    public Skill toEntity() {
        return Skill.builder()
                .id(this.id)
                .name(this.name)
                .category(this.category)
                .description(this.description)
                .build();
    }
}
