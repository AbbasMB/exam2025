package dat.dtos;

import dat.entities.Candidate;
import lombok.*;

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

    public CandidateDTO(Candidate c) {
        this.id = c.getId();
        this.name = c.getName();
        this.phone = c.getPhone();
        this.education = c.getEducation();
    }

    public Candidate toEntity() {
        return Candidate.builder()
                .id(this.id)
                .name(this.name)
                .phone(this.phone)
                .education(this.education)
                .build();
    }
}
