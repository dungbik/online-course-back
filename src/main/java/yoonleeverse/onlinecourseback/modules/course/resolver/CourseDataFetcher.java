package yoonleeverse.onlinecourseback.modules.course.resolver;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.resolver.dataloaders.PrerequisiteDataLoader;
import yoonleeverse.onlinecourseback.modules.course.resolver.dataloaders.VideoCategoryDataLoader;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.CommentType;
import yoonleeverse.onlinecourseback.modules.course.types.VideoType;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCommentInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.course.types.VideoCategoryType;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCommentInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCourseInput;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@DgsComponent
@RequiredArgsConstructor
public class CourseDataFetcher {

    private final CourseService courseService;

    @DgsData(parentType = "CourseType")
    public CompletableFuture<List<CourseType>> prerequisite(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, List<CourseType>> PrerequisitesDataLoader = dfe.getDataLoader(PrerequisiteDataLoader.class);
        CourseType course = dfe.getSource();

        return PrerequisitesDataLoader.load(course.getSlug());
    }

    @DgsData(parentType = "CourseType")
    public CompletableFuture<List<VideoCategoryType>> videoCategories(DgsDataFetchingEnvironment dfe) {
        DataLoader<String, List<VideoCategoryType>> videoCategoriesDataLoader = dfe.getDataLoader(VideoCategoryDataLoader.class);
        CourseType course = dfe.getSource();

        return videoCategoriesDataLoader.load(course.getSlug());
    }

    @DgsQuery
    public List<CourseType> getAllCourse() {
        return courseService.getAllCourse();
    }

    @DgsQuery
    public CourseType getCourse(@InputArgument String slug) {
        return courseService.getCourse(slug);
    }

    @DgsQuery
    public List<CommentType> getAllComment(@InputArgument String videoId) {
        return courseService.getAllComment(videoId);
    }

    @DgsQuery
    public VideoType getVideo(@InputArgument String videoId) {
        return courseService.getVideo(videoId);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType addCourse(@InputArgument AddCourseInput input) {
        return courseService.addCourse(input);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType updateCourse(@InputArgument UpdateCourseInput input) {
        return courseService.updateCourse(input);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public ResultType removeCourse(@InputArgument String slug) {
        return courseService.removeCourse(slug);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    public ResultType addComment(@InputArgument AddCommentInput input) {
        return courseService.addComment(input);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    public ResultType updateComment(@InputArgument UpdateCommentInput input) {
        return courseService.updateComment(input);
    }

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    public ResultType removeComment(@InputArgument String commentId) {
        return courseService.removeComment(commentId);
    }
}
