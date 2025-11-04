package dat.entities;

import dat.enums.SkillCategory;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    private String description;

    private String slug;
    private Integer popularityScore;
    private Integer averageSalary;

    // Relations
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CandidateSkill> candidateLinks = new HashSet<>();

    // Bidirectional
    public void addCandidate(Candidate candidate) {
        CandidateSkill link = new CandidateSkill();
        link.setSkill(this);
        link.setCandidate(candidate);
        this.candidateLinks.add(link);
        candidate.getSkillLinks().add(link);
    }

    public void removeCandidate(Candidate candidate) {
        candidateLinks.removeIf(link -> link.getCandidate().equals(candidate));
        candidate.getSkillLinks().removeIf(link -> link.getSkill().equals(this));
    }
}
