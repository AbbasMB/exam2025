package dat.services;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillStat {
    private String id;
    private String slug;
    private String name;
    private String categoryKey;
    private String description;
    private int popularityScore;
    private int averageSalary;
    private String updatedAt;
}
