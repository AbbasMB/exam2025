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

    public static void setEMF(EntityManagerFactory emf) {
        if (EMF != null && EMF.equals(emf)) {
            return;
        }
        EMF = emf;
        candidateDAO = new CandidateDAO(emf);
    }

    public static void populate() {
        data = new ArrayList<>();

        data.add(Candidate.builder().name("Abbas Badreddine").phone("11111111").education("Datamatiker").build());
        data.add(Candidate.builder().name("Fatima Rahman").phone("22222222").education("Softwareudvikler").build());
        data.add(Candidate.builder().name("Jonas Møller").phone("33333333").education("It-supporter").build());
        data.add(Candidate.builder().name("Layla Hassan").phone("44444444").education("Webudvikler").build());
        data.add(Candidate.builder().name("Khaled Omar").phone("55555555").education("Datavidenskab").build());
        data.add(Candidate.builder().name("Sofie Jensen").phone("66666666").education("Frontend udvikler").build());
        data.add(Candidate.builder().name("Yousef Nasser").phone("77777777").education("Backend udvikler").build());
        data.add(Candidate.builder().name("Mona Suwan").phone("88888888").education("Systemanalytiker").build());
        data.add(Candidate.builder().name("Amir Hassan").phone("99999999").education("Softwareingeniør").build());
        data.add(Candidate.builder().name("Freja Andersen").phone("10101010").education("Test specialist").build());
        data.add(Candidate.builder().name("Karim Zidan").phone("12121212").education("Database administrator").build());
        data.add(Candidate.builder().name("Emma Holm").phone("13131313").education("Cloud arkitekt").build());
        data.add(Candidate.builder().name("Omar Ali").phone("14141414").education("Netværksspecialist").build());
        data.add(Candidate.builder().name("Ida Rasmussen").phone("15151515").education("Full Stack udvikler").build());
        data.add(Candidate.builder().name("Amina Noor").phone("16161616").education("Dataanalytiker").build());

        data.forEach(candidateDAO::create);

        if (SkillPopulator.isHasPopulated()) {
            List<Skill> skills = SkillPopulator.fetch();

            for (int i = 0; i < data.size(); i++) {
                int skillIndex = i % skills.size();
                candidateDAO.linkSkillToCandidate(data.get(i).getId(), skills.get(skillIndex).getId());
            }
        }
    }

    public static List<Candidate> fetch() {
        return candidateDAO.getAll();
    }
}
