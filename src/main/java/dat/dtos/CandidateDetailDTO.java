package dat.dtos;

import dat.entities.Candidate;
import lombok.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CandidateDetailDTO {
    private int id;
    private String name;
    private String phone;
    private String education;
    private List<SkillDTO> skills;

    public CandidateDetailDTO(Candidate c) {
        this.id = c.getId();
        this.name = c.getName();
        this.phone = c.getPhone();
        this.education = c.getEducation();

        this.skills = c.getSkillLinks().stream()
                .map(link -> new SkillDTO(link.getSkill()))
                .collect(Collectors.toList());
    }
}
