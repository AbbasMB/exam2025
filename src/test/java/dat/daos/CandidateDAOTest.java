package dat.daos;

import dat.config.HibernateConfig;
import dat.entities.Candidate;
import dat.populator.CandidatePopulator;
import dat.populator.SkillPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("IntegrationTest")
class CandidateDAOTest {

    static private EntityManagerFactory emf;
    static private CandidateDAO dao;

    @BeforeAll
    static void setupOnce() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = new CandidateDAO(emf);
    }

    @BeforeEach
    void setup() {
        SkillPopulator.setEMF(emf);
        SkillPopulator.populate();

        CandidatePopulator.setEMF(emf);
        CandidatePopulator.populate();
    }

    @AfterEach
    void teardown() {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("TRUNCATE TABLE candidateskill, candidate, skill RESTART IDENTITY CASCADE")
                .executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    @AfterAll
    static void tearDownOnce() {
        if (emf != null) emf.close();
    }

    @Test
    void read() {
        Candidate expected = CandidatePopulator.fetch().get(0);
        Candidate actual = dao.getById(expected.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPhone(), actual.getPhone());
    }

    @Test
    void readAll() {
        List<Candidate> expected = CandidatePopulator.fetch();
        List<Candidate> actual = dao.getAll();
        assertThat(actual, containsInAnyOrder(expected.toArray()));
    }

    @Test
    void create() {
        Candidate newCandidate = Candidate.builder()
                .name("Test Kandidat")
                .phone("99999999")
                .education("Softwareudvikling")
                .build();

        Candidate created = dao.create(newCandidate);
        Candidate actualInDb = dao.getById(created.getId());

        assertNotNull(created.getId());
        assertEquals("Test Kandidat", actualInDb.getName());
        assertEquals(created.getName(), actualInDb.getName());
    }

    @Test
    void update() {
        Candidate candidate = CandidatePopulator.fetch().get(0);
        candidate.setEducation("Opdateret uddannelse");

        Candidate updated = dao.update(candidate);
        Candidate actualInDb = dao.getById(candidate.getId());

        assertEquals("Opdateret uddannelse", actualInDb.getEducation());
        assertEquals(updated.getEducation(), actualInDb.getEducation());
    }

    @Test
    void delete() {
        Candidate candidate = CandidatePopulator.fetch().get(0);
        dao.delete(candidate.getId());

        Exception ex = assertThrows(Exception.class, () -> dao.getById(candidate.getId()));
        assertTrue(ex.getMessage().contains("not found"));
    }
}
