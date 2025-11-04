package dat.controllers;

import dat.daos.CandidateDAO;
import dat.dtos.CandidateDTO;
import dat.dtos.CandidateMarketDataDTO;
import dat.entities.Candidate;
import dat.enums.SkillCategory;
import dat.exceptions.DatabaseException;
import dat.services.FetchTools;
import dat.services.SkillStatsService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CandidateController implements IController {

    private static CandidateController instance;
    private CandidateDAO candidateDAO;
    private EntityManagerFactory emf;
    private SkillStatsService skillStatsService;
    CandidateMarketDataDTO candidateMarketDataDTO;

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
        this.skillStatsService = new SkillStatsService(new FetchTools());
    }

    @Override
    public void getAll(Context ctx) {
        List<CandidateDTO> dtos = candidateDAO.getAll()
                .stream()
                .map(CandidateDTO::new)
                .toList();
        ctx.status(200).json(dtos);
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

    public void getCandidateWithMarketData(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Candidate candidate = candidateDAO.getById(id);
        if (candidate == null) {
            throw new EntityNotFoundException("Candidate with id " + id + " not found");
        }
        List<String> slugs = candidate.getSkillLinks().stream()
                .map(link -> link.getSkill().getSlug())
                .filter(Objects::nonNull)
                .toList();
        if (!slugs.isEmpty()) {
            try {
                var response = skillStatsService.getSkillStats(slugs);
                // Match market data til lokale skills
                candidate.getSkillLinks().forEach(link -> {
                    var skill = link.getSkill();
                    response.getData().stream()
                            .filter(stat -> stat.getSlug().equalsIgnoreCase(skill.getSlug()))
                            .findFirst()
                            .ifPresent(stat -> {
                                skill.setPopularityScore(stat.getPopularityScore());
                                skill.setAverageSalary(stat.getAverageSalary());
                            });
                });
            } catch (Exception e) {
                throw new DatabaseException(500, "Failed to fetch skill statistics", e);
            }
        }
        CandidateDTO dto = new CandidateDTO(candidate);
        ctx.status(200).json(dto);
    }
}
