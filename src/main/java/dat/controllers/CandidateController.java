package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.CandidateDAO;
import dat.daos.SkillDAO;
import dat.entities.Candidate;
import dat.enums.SkillCategory;
import dat.exceptions.DatabaseException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;

public class CandidateController implements IController {

    private static CandidateController instance;
    private CandidateDAO candidateDAO;
    private EntityManagerFactory emf;

    private CandidateController() {}

    public static CandidateController getInstance() {
        if (instance == null) {
            instance = new CandidateController();
        }
        return instance;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
        this.candidateDAO = new CandidateDAO(emf);
    }

    @Override
    public void getAll(Context ctx) {
        List<Candidate> candidates = candidateDAO.getAll();
        ctx.json(candidates);
        ctx.status(200);
    }

    @Override
    public void getById(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Candidate candidate = candidateDAO.getById(id);
        ctx.json(candidate);
        ctx.status(200);
    }

    @Override
    public void create(Context ctx) {
        Candidate candidate = ctx.bodyAsClass(Candidate.class);
        Candidate created = candidateDAO.create(candidate);
        ctx.json(created);
        ctx.status(201);
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Candidate updatedData = ctx.bodyAsClass(Candidate.class);
        updatedData.setId(id);

        Candidate updated = candidateDAO.update(updatedData);
        ctx.json(updated);
        ctx.status(200);
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        candidateDAO.delete(id);
        ctx.status(204);
    }

    public void linkSkill(Context ctx) {
        int candidateId = ctx.pathParamAsClass("candidateId", Integer.class).get();
        int skillId = ctx.pathParamAsClass("skillId", Integer.class).get();

        try {
            Candidate updated = candidateDAO.linkSkillToCandidate(candidateId, skillId);
            ctx.json(updated);
            ctx.status(200);
        } catch (Exception e) {
            throw new DatabaseException(500, "Could not link candidate and skill", e);
        }
    }

    public void getFilteredCandidates(Context ctx) {
        List<Candidate> candidates = candidateDAO.getAll();
        String categoryParam = ctx.queryParam("category");
        if (categoryParam != null && !categoryParam.isBlank()) {
            try {
                SkillCategory category = SkillCategory.valueOf(categoryParam.toUpperCase());
                candidates = candidates.stream()
                        .filter(candidate -> candidate.getSkillLinks().stream()
                                .anyMatch(link -> link.getSkill().getCategory() == category))
                        .toList();
                ctx.json(candidates);
                ctx.status(200);
            } catch (IllegalArgumentException e) {
                ctx.status(400);
                ctx.json(Map.of(
                        "status", 400,
                        "error", "Invalid category: " + categoryParam
                ));
                return;
            }
        } else {
            ctx.json(candidates);
            ctx.status(200);
        }
    }

}
