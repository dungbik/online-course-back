package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class CourseDataFetcher {

    private final CourseService courseService;

    @DgsQuery
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public List<CourseType> getAllCourse() {
        return courseService.getAllCourse();
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType addCourse(@InputArgument AddCourseInput input) {
        return courseService.addCourse(input);
    }

}
