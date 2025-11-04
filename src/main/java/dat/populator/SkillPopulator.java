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
        if (hasPopulated) return;

        data = new ArrayList<>();

        data.add(Skill.builder()
                .name("Java")
                .slug("java")
                .category(SkillCategory.PROG_LANG)
                .description("General-purpose, strongly-typed language for backend and Android.")
                .build());

        data.add(Skill.builder()
                .name("Spring Boot")
                .slug("spring-boot")
                .category(SkillCategory.FRAMEWORK)
                .description("Java framework for building microservices and REST APIs.")
                .build());

        data.add(Skill.builder()
                .name("PostgreSQL")
                .slug("postgresql")
                .category(SkillCategory.DB)
                .description("Open-source relational database with strong SQL compliance.")
                .build());

        data.add(Skill.builder()
                .name("Docker")
                .slug("docker")
                .category(SkillCategory.DEVOPS)
                .description("Container platform for deploying and running applications.")
                .build());

        data.add(Skill.builder()
                .name("JUnit")
                .slug("junit")
                .category(SkillCategory.TESTING)
                .description("Unit testing framework for Java applications.")
                .build());

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
