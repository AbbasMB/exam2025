package dat.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Skill skill;
}
