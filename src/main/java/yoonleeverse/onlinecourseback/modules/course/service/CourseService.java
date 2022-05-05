package yoonleeverse.onlinecourseback.modules.course.service;

import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.types.*;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCommentInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCommentInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCourseInput;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<CourseType> getAllCourse();

    CourseType getCourse(String slug);

    ResultType addCourse(AddCourseInput input, MultipartFile file);

    Map<String, List<TechType>> techsForCourses(List<String> slugs);

    Map<String, List<CourseType>> prerequisitesForCourses(List<String> slugs);

    ResultType updateCourse(UpdateCourseInput input, MultipartFile file);

    ResultType removeCourse(String courseId);

    ResultType addComment(AddCommentInput input);

    ResultType updateComment(UpdateCommentInput input);

    ResultType removeComment(String commentId);

    List<CommentType> getAllComment(Long videoId);

    VideoType getVideo(Long videoId);

    ResultType completeVideo(Long videoId);
}
