package yoonleeverse.onlinecourseback.modules.course.service;

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

    CourseType getCourse(String courseId);

    ResultType addCourse(AddCourseInput input);

    Map<String, List<TechType>> techsForCourses(List<String> courseIds);

    Map<String, List<CourseType>> prerequisitesForCourses(List<String> courseIds);

    Map<String, List<VideoCategoryType>> videoCategoriesForCourses(List<String> courseIds);

    ResultType updateCourse(UpdateCourseInput input);

    ResultType removeCourse(String courseId);

    ResultType addComment(AddCommentInput input);

    ResultType updateComment(UpdateCommentInput input);

    ResultType removeComment(String commentId);

    List<CommentType> getAllComment(String videoId);

    VideoType getVideo(String videoId);
}
