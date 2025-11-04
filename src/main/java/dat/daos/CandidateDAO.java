package dat.daos;

import dat.entities.Candidate;
import dat.entities.Skill;
import dat.exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CandidateDAO implements IDAO<Candidate, Integer> {

    private final EntityManagerFactory emf;

    public CandidateDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Candidate create(Candidate entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            throw new DatabaseException("Failed to create candidate", e);
        }
    }

    @Override
    public Candidate update(Candidate entity) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate updated = em.merge(entity);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update candidate", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate == null) {
                throw new DatabaseException("Candidate with id " + id + " not found");
            }
            em.getTransaction().begin();
            em.remove(candidate);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete candidate", e);
        }
    }

    @Override
    public Candidate getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT DISTINCT c FROM Candidate c " +
                                    "LEFT JOIN FETCH c.skillLinks sl " +
                                    "LEFT JOIN FETCH sl.skill s " +
                                    "WHERE c.id = :id",
                            Candidate.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch candidate with id " + id, e);
        }
    }

    public List<Candidate> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                    "SELECT DISTINCT c FROM Candidate c LEFT JOIN FETCH c.skillLinks sl LEFT JOIN FETCH sl.skill s",
                    Candidate.class
            ).getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch candidates", e);
        }
    }


    public Candidate linkSkillToCandidate(int candidateId, int skillId) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, candidateId);
            Skill skill = em.find(Skill.class, skillId);
            if (candidate == null) {
                throw new DatabaseException("Candidate with id " + candidateId + " not found");
            }
            if (skill == null) {
                throw new DatabaseException("Skill with id " + skillId + " not found");
            }
            boolean alreadyLinked = candidate.getSkillLinks().stream()
                    .anyMatch(link -> link.getSkill().getId() == skillId);
            if (!alreadyLinked) {
                candidate.addSkill(skill);
                em.merge(candidate);
            }
            em.getTransaction().commit();
            return candidate;
        } catch (Exception e) {
            throw new DatabaseException("Failed to link candidate and skill", e);
        }
    }
}
