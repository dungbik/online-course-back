package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;
import yoonleeverse.onlinecourseback.modules.course.service.CourseTechService;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "mainTechs")
@RequiredArgsConstructor
@Slf4j
public class MainTechDataLoaderWithContext implements MappedBatchLoaderWithContext<String, List<TechType>> {

    private final CourseTechService courseTechService;

    @Override
    public CompletionStage<Map<String, List<TechType>>> load(Set<String> keys, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> courseTechService.techsForCourses(new ArrayList<>(keys)));
    }
}