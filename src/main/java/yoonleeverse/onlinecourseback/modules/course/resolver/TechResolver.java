package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.service.TechService;
import yoonleeverse.onlinecourseback.modules.course.types.AddTechInput;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class TechResolver {

    private final TechService techService;

    @DgsQuery
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public List<TechType> getAllTech() {
        return techService.getAllTech();
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType addTech(AddTechInput input) {
        return techService.addTech(input);
    }
}
