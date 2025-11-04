package dat.routes;

import dat.controllers.CandidateController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CandidateRoutes {

    private static CandidateController controller;

    public static EndpointGroup getRoutes() {
        controller = CandidateController.getInstance();
        return () -> {
            get("/", controller::getAll,  Role.ANYONE);
            get("/filteredCategories", controller::getFilteredCandidates, Role.USER);
            get("/{id}/marketdata", controller::getCandidateWithMarketData, Role.ANYONE);
            get("/{id}", controller::getById, Role.ANYONE);
            post("/", controller::create, Role.USER);
            put("/{id}", controller::update, Role.USER);
            delete("/{id}", controller::delete, Role.USER);
            put("/{candidateId}/skills/{skillId}", controller::linkSkill, Role.USER);
        };
    }
}
