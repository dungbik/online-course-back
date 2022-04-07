package yoonleeverse.onlinecourseback.modules.course.resolver.dataloaders;

import com.netflix.graphql.dgs.DgsDataLoader;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoaderEnvironment;
import org.dataloader.MappedBatchLoaderWithContext;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.course.types.VideoCategoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@DgsDataLoader
@RequiredArgsConstructor
public class VideoCategoryDataLoader implements MappedBatchLoaderWithContext<String, List<VideoCategoryType>> {

    private final CourseService courseService;

    @Override
    public CompletionStage<Map<String, List<VideoCategoryType>>> load(Set<String> keys, BatchLoaderEnvironment environment) {
        return CompletableFuture.supplyAsync(() -> courseService.videoCategoriesForCourses(new ArrayList<>(keys)));
    }
}
