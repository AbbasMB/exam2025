package dat.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Relations

    @ManyToOne(fetch = FetchType.EAGER)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Skill skill;
}
