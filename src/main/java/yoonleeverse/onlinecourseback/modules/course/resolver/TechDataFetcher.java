package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.resolver.dataloaders.MainTechDataLoader;
import yoonleeverse.onlinecourseback.modules.course.service.TechService;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddTechInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class TechDataFetcher {

    private final TechService techService;

    @DgsData(parentType = "CourseType")
    public CompletableFuture<List<TechType>> mainTechs(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, List<TechType>> mainTechsDataLoader = dfe.getDataLoader(MainTechDataLoader.class);
        CourseType course = dfe.getSource();

        return mainTechsDataLoader.load(course.getSlug());
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public List<TechType> getAllTech() {
        return techService.getAllTech();
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType addTech(@InputArgument AddTechInput input) {
        return techService.addTech(input);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType removeTech(@InputArgument Long id) {
        return techService.removeTech(id);
    }
}
