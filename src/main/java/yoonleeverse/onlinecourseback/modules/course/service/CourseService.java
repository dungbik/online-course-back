package yoonleeverse.onlinecourseback.modules.course.service;

import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;
import yoonleeverse.onlinecourseback.modules.course.types.VideoCategoryType;
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
}
