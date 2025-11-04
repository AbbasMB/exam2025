package dat.populator;

import dat.daos.CandidateDAO;
import dat.entities.Candidate;
import dat.entities.Skill;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;

public class CandidatePopulator {

    private static EntityManagerFactory EMF;
    private static CandidateDAO candidateDAO;
    private static List<Candidate> data = new ArrayList<>();
    private static boolean hasPopulated = false;

    public static void setEMF(EntityManagerFactory emf) {
        if (EMF != null && EMF.equals(emf)) {
            return;
        }
        EMF = emf;
        candidateDAO = new CandidateDAO(emf);
        hasPopulated = false;
    }

    public static void populate() {
        if (hasPopulated) return;

        data = new ArrayList<>();

        data.add(Candidate.builder().name("Abbas Badreddine").phone("11111111").education("Computer Science").build());
        data.add(Candidate.builder().name("Fatima Rahman").phone("22222222").education("Software Developer").build());
        data.add(Candidate.builder().name("Jonas Møller").phone("33333333").education("IT Support").build());
        data.add(Candidate.builder().name("Layla Hassan").phone("44444444").education("Web Developer").build());
        data.add(Candidate.builder().name("Khaled Omar").phone("55555555").education("Data Science").build());
        data.add(Candidate.builder().name("Sofie Jensen").phone("66666666").education("Frontend Developer").build());
        data.add(Candidate.builder().name("Yousef Nasser").phone("77777777").education("Backend Developer").build());
        data.add(Candidate.builder().name("Mona Suwan").phone("88888888").education("Systems Analyst").build());
        data.add(Candidate.builder().name("Amir Hassan").phone("99999999").education("Software Engineer").build());
        data.add(Candidate.builder().name("Freja Andersen").phone("10101010").education("Test Specialist").build());
        data.add(Candidate.builder().name("Karim Zidan").phone("12121212").education("Database Administrator").build());
        data.add(Candidate.builder().name("Emma Holm").phone("13131313").education("Cloud Architect").build());
        data.add(Candidate.builder().name("Omar Ali").phone("14141414").education("Network Specialist").build());
        data.add(Candidate.builder().name("Ida Rasmussen").phone("15151515").education("Full Stack Developer").build());
        data.add(Candidate.builder().name("Amina Noor").phone("16161616").education("Data Analyst").build());


        data.forEach(candidateDAO::create);

        if (SkillPopulator.isHasPopulated()) {
            List<Skill> skills = SkillPopulator.fetch();

            for (int i = 0; i < data.size(); i++) {
                Candidate candidate = data.get(i);
                Skill skill = skills.get(i % skills.size());
                candidateDAO.linkSkillToCandidate(candidate.getId(), skill.getId());
            }
        }
    }

    public static List<Candidate> fetch() {
        return candidateDAO.getAll();
    }

    public static boolean isHasPopulated() {
        return hasPopulated;
    }
}
