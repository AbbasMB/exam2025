package dat.daos;

import dat.entities.Candidate;
import dat.entities.CandidateSkill;
import dat.entities.Skill;
import dat.exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CandidateDAO implements IDAO<Candidate, Integer> {

    private final EntityManagerFactory emf;

    public CandidateDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Candidate create(Candidate candidate) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(candidate);
            em.getTransaction().commit();
            return candidate;
        } catch (Exception e) {
            throw new DatabaseException("Failed to create candidate", e);
        }
    }

    @Override
    public Candidate update(Candidate candidate) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate updated = em.merge(candidate);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update candidate with id " + candidate.getId(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate == null) {
                throw new DatabaseException("Candidate with id " + id + " not found");
            }
            em.remove(candidate);
            em.getTransaction().commit();
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete candidate with id " + id, e);
        }
    }

    @Override
    public Candidate getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Candidate candidate = em.find(Candidate.class, id);
            if (candidate == null) {
                throw new DatabaseException("Candidate with id " + id + " not found");
            }
            return candidate;
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error fetching candidate with id " + id, e);
        }
    }

    @Override
    public List<Candidate> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                    "SELECT DISTINCT c FROM Candidate c " +
                            "LEFT JOIN FETCH c.skillLinks sl " +
                            "LEFT JOIN FETCH sl.skill",
                    Candidate.class
            ).getResultList();
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
            candidate.addSkill(skill);
            em.merge(candidate);
            em.getTransaction().commit();
            return candidate;
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to link candidate " + candidateId + " and skill " + skillId, e);
        }
    }
}
