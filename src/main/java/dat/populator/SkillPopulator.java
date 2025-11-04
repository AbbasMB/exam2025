package dat.populator;

import dat.daos.SkillDAO;
import dat.entities.Skill;
import dat.enums.SkillCategory;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class SkillPopulator {

    private static EntityManagerFactory EMF;
    private static SkillDAO skillDAO;
    private static List<Skill> data = new ArrayList<>();
    private static boolean hasPopulated = false;

    public static void setEMF(EntityManagerFactory emf) {
        if (EMF != null && EMF.equals(emf)) {
            return;
        }
        EMF = emf;
        skillDAO = new SkillDAO(emf);
        hasPopulated = false;
    }

    public static void populate() {
        data = new ArrayList<>();

        data.add(Skill.builder().name("Java").category(SkillCategory.PROG_LANG).description("Programming language").build());
        data.add(Skill.builder().name("PostgreSQL").category(SkillCategory.DB).description("Relational database system").build());
        data.add(Skill.builder().name("Docker").category(SkillCategory.DEVOPS).description("Containerization tool").build());
        data.add(Skill.builder().name("HTML").category(SkillCategory.FRONTEND).description("Frontend markup language").build());
        data.add(Skill.builder().name("JUnit").category(SkillCategory.TESTING).description("Testing framework for Java").build());
        data.add(Skill.builder().name("Power BI").category(SkillCategory.DATA).description("Data analysis and visualization tool").build());
        data.add(Skill.builder().name("Spring Boot").category(SkillCategory.FRAMEWORK).description("Java web framework").build());

        data.forEach(skillDAO::create);
        hasPopulated = true;
    }

    public static List<Skill> fetch() {
        return skillDAO.getAll();
    }

    public static boolean isHasPopulated() {
        return hasPopulated;
    }
}
