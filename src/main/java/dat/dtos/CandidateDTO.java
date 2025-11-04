package dat.dtos;

import dat.entities.Candidate;
import dat.entities.CandidateSkill;
import dat.entities.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
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
                .map(CandidateSkill::getSkill)
                .collect(Collectors.toMap(
                        Skill::getId,              // key = skill id
                        SkillDTO::new,             // value = skill dto
                        (existing, duplicate) -> existing // keep first if duplicate
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }
}
