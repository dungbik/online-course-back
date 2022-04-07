package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class CourseDataFetcher {

    private final CourseService courseService;

    @DgsData(parentType = "CourseType")
    public CompletableFuture<List<CourseType>> prerequisite(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, List<CourseType>> PrerequisitesDataLoader = dfe.getDataLoader(PrerequisiteDataLoaderWithContext.class);
        CourseType course = dfe.getSource();

        return PrerequisitesDataLoader.load(course.getCourseId());
    }

    @DgsQuery
    public List<CourseType> getAllCourse() {
        return courseService.getAllCourse();
    }

    @DgsQuery
    public CourseType getCourse(@InputArgument String courseId) {
        return courseService.getCourse(courseId);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType addCourse(@InputArgument AddCourseInput input) {
        return courseService.addCourse(input);
    }

}
