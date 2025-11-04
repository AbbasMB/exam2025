package dat.daos;

import dat.entities.Skill;
import dat.exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class SkillDAO implements IDAO<Skill, Integer> {

    private final EntityManagerFactory emf;

    public SkillDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Skill create(Skill skill) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(skill);
            em.getTransaction().commit();
            return skill;
        } catch (Exception e) {
            throw new DatabaseException("Failed to create skill", e);
        }
    }

    @Override
    public Skill update(Skill skill) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill merged = em.merge(skill);
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            throw new DatabaseException("Failed to update skill with id " + skill.getId(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Skill skill = em.find(Skill.class, id);
            if (skill == null) {
                throw new DatabaseException("Skill with id " + id + " not found");
            }
            em.remove(skill);
            em.getTransaction().commit();
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Failed to delete skill with id " + id, e);
        }
    }

    @Override
    public Skill getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Skill skill = em.find(Skill.class, id);
            if (skill == null) {
                throw new DatabaseException("Skill with id " + id + " not found");
            }
            return skill;
        } catch (DatabaseException e) {
            throw e;
        } catch (Exception e) {
            throw new DatabaseException("Error fetching skill with id " + id, e);
        }
    }

    @Override
    public List<Skill> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Skill> query = em.createQuery("SELECT s FROM Skill s LEFT JOIN FETCH s.candidateLinks", Skill.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DatabaseException("Failed to fetch all skills", e);
        }
    }
}
