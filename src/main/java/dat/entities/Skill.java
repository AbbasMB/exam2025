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
@EqualsAndHashCode(of = {"id"})
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Enumerated(EnumType.STRING)
    private SkillCategory category;

    private String description;

    //Relations

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CandidateSkill> candidateLinks = new HashSet<>();

    //Bidirectional

    public void addCandidateLink(CandidateSkill link) {
        this.candidateLinks.add(link);
        if (link != null) {
            link.setSkill(this);
        }
    }

    public void removeCandidateLink(CandidateSkill link) {
        this.candidateLinks.remove(link);
        if (link != null) {
            link.setSkill(null);
        }
    }
}
