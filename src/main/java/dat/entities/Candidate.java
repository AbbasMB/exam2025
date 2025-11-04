package dat.entities;

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
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String phone;
    private String education;

    //Relations

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<CandidateSkill> skillLinks = new HashSet<>();

    //Bidirectional

    public void addSkillLink(CandidateSkill link) {
        this.skillLinks.add(link);
        if (link != null) {
            link.setCandidate(this);
        }
    }

    public void removeSkillLink(CandidateSkill link) {
        this.skillLinks.remove(link);
        if (link != null) {
            link.setCandidate(null);
        }
    }
}
