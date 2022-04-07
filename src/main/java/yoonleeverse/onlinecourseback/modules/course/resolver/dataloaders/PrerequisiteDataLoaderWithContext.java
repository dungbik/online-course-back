package yoonleeverse.onlinecourseback.modules.course.resolver.dataloaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader(name = "prerequisite")
@RequiredArgsConstructor
@Slf4j
public class PrerequisiteDataLoaderWithContext implements MappedBatchLoaderWithContext<String, List<CourseType>> {

    private final CourseService courseService;

    @Override
    public CompletionStage<Map<String, List<CourseType>>> load(Set<String> keys, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> courseService.prerequisitesForCourses(new ArrayList<>(keys)));
    }
}