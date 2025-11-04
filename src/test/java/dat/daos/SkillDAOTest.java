package dat.daos;

import dat.config.HibernateConfig;
import dat.entities.Skill;
import dat.enums.SkillCategory;
import dat.populator.SkillPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
class SkillDAOTest {

    static private EntityManagerFactory emf;
    static private SkillDAO dao;

    @BeforeAll
    static void setupOnce() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new SkillDAO(emf);
    }

    @BeforeEach
    void setup() {
        SkillPopulator.setEMF(emf);
        SkillPopulator.populate();
    }

    @AfterEach
    void teardown() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("TRUNCATE TABLE candidateskill, candidate, skill RESTART IDENTITY CASCADE").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @AfterAll
    static void tearDownOnce() {
        if (emf != null) emf.close();
    }

    @Test
    void read() {
        Skill expected = SkillPopulator.fetch().get(0);
        Skill actual = dao.getById(expected.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCategory(), actual.getCategory());
    }

    @Test
    void readAll() {
        List<Skill> expected = SkillPopulator.fetch();
        List<Skill> actual = dao.getAll();
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void create() {
        Skill newSkill = Skill.builder()
                .name("Kubernetes")
                .category(SkillCategory.DEVOPS)
                .description("Container orchestration platform")
                .build();

        Skill created = dao.create(newSkill);
        Skill actualInDb = dao.getById(created.getId());

        assertNotNull(created.getId());
        assertEquals("Kubernetes", actualInDb.getName());
        assertEquals(created.getCategory(), actualInDb.getCategory());
    }

    @Test
    void update() {
        Skill skill = SkillPopulator.fetch().get(0);
        skill.setDescription("Updated description");

        Skill updated = dao.update(skill);
        Skill actualInDb = dao.getById(skill.getId());

        assertEquals("Updated description", actualInDb.getDescription());
        assertEquals(updated.getDescription(), actualInDb.getDescription());
    }

    @Test
    void delete() {
        Skill skill = SkillPopulator.fetch().get(0);
        dao.delete(skill.getId());

        Exception ex = assertThrows(Exception.class, () -> dao.getById(skill.getId()));
        assertTrue(ex.getMessage().contains("not found"));
    }
}
