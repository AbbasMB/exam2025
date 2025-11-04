package dat.dtos;

import dat.entities.Candidate;
import dat.entities.CandidateSkill;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDTO {
    private int id;
    private String name;
    private String phone;
    private String education;
    private List<SkillDTO> skills;

    public CandidateDTO(Candidate candidate) {
        this.id = candidate.getId();
        this.name = candidate.getName();
        this.phone = candidate.getPhone();
        this.education = candidate.getEducation();

        this.skills = candidate.getSkillLinks().stream()
                .map(link -> new SkillDTO(link.getSkill()))
                .toList();
    }
}
