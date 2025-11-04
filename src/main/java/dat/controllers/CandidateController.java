package dat.controllers;

import dat.daos.CandidateDAO;
import dat.dtos.CandidateDTO;
import dat.entities.Candidate;
import dat.entities.Skill;
import dat.enums.SkillCategory;
import dat.exceptions.DatabaseException;
import dat.services.SkillStatsService;
import dat.dtos.SkillStatsResponseDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CandidateController implements IController {

    private static CandidateController instance; // ✅ Singleton instance
    private CandidateDAO candidateDAO;
    private final SkillStatsService skillStatsService;

    private CandidateController() {
        this.skillStatsService = SkillStatsService.getInstance();
    }

    public static CandidateController getInstance() {
        if (instance == null) {
            instance = new CandidateController();
        }
        return instance;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.candidateDAO = new CandidateDAO(emf);
    }

    @Override
    public void getAll(Context ctx) {
        List<Candidate> candidates = candidateDAO.getAll();
        ctx.status(200).json(candidates.stream().map(CandidateDTO::new).toList());
    }

    @Override
    public void getById(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Candidate candidate = candidateDAO.getById(id);
        ctx.status(200).json(new CandidateDTO(candidate));
    }

    @Override
    public void create(Context ctx) {
        Candidate candidate = ctx.bodyAsClass(Candidate.class);
        Candidate created = candidateDAO.create(candidate);
        ctx.status(201).json(new CandidateDTO(created));
    }

    @Override
    public void update(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        Candidate body = ctx.bodyAsClass(Candidate.class);
        body.setId(id);
        Candidate updated = candidateDAO.update(body);
        ctx.status(200).json(new CandidateDTO(updated));
    }

    @Override
    public void delete(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        candidateDAO.delete(id);
        ctx.status(204);
    }

    public void getFilteredCandidates(Context ctx) {
        List<Candidate> candidates = candidateDAO.getAll();
        String categoryParam = ctx.queryParam("category");
        if (categoryParam != null && !categoryParam.isBlank()) {
            try {
                SkillCategory category = SkillCategory.valueOf(categoryParam.toUpperCase());
                candidates = candidates.stream()
                        .filter(c -> c.getSkillLinks().stream()
                                .anyMatch(l -> l.getSkill().getCategory() == category))
                        .toList();
            } catch (IllegalArgumentException e) {
                ctx.status(400).json(Map.of(
                        "status", 400,
                        "error", "Invalid category: " + categoryParam
                ));
                return;
            }
        }
        ctx.status(200).json(candidates.stream().map(CandidateDTO::new).toList());
    }

    public void linkSkill(Context ctx) {
        int candidateId = ctx.pathParamAsClass("candidateId", Integer.class).get();
        int skillId = ctx.pathParamAsClass("skillId", Integer.class).get();

        try {
            Candidate updatedCandidate = candidateDAO.linkSkillToCandidate(candidateId, skillId);
            ctx.status(200).json(new CandidateDTO(updatedCandidate));
        } catch (DatabaseException e) {
            ctx.status(400).json(Map.of(
                    "status", 400,
                    "error", e.getMessage()
            ));
        } catch (Exception e) {
            ctx.status(500).json(Map.of(
                    "status", 500,
                    "error", "Unexpected error linking skill to candidate"
            ));
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
                SkillStatsResponseDTO response = skillStatsService.getSkillStats(slugs);

                if (response == null || response.getData() == null) {
                    throw new DatabaseException(500, "No market data returned from API");
                }
                candidate.getSkillLinks().forEach(link -> {
                    Skill skill = link.getSkill();
                    response.getData().stream()
                            .filter(stat -> stat.getSlug().equalsIgnoreCase(skill.getSlug()))
                            .findFirst()
                            .ifPresent(stat -> {
                                skill.setPopularityScore(stat.getPopularityScore());
                                skill.setAverageSalary(stat.getAverageSalary());
                            });
                });
            } catch (IOException | InterruptedException e) {
                throw new DatabaseException(500, "Failed to fetch skill statistics from external API", e);
            } catch (Exception e) {
                throw new DatabaseException(500, "Unexpected error while fetching skill statistics", e);
            }
        }
        CandidateDTO dto = new CandidateDTO(candidate);
        ctx.status(200).json(dto);
    }

}
