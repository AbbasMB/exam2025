package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String phone;
    private String education;

    // Relations
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CandidateSkill> skillLinks;

    // Bidirectional
    public void addSkill(Skill skill) {
        CandidateSkill link = new CandidateSkill();
        link.setCandidate(this);
        link.setSkill(skill);
        this.skillLinks.add(link);
        skill.getCandidateLinks().add(link);
    }

    public void removeSkill(Skill skill) {
        skillLinks.removeIf(link -> link.getSkill().equals(skill));
        skill.getCandidateLinks().removeIf(link -> link.getCandidate().equals(this));
    }
}
