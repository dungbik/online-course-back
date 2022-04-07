package yoonleeverse.onlinecourseback.modules.course.service;

import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.types.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.List;
import java.util.Map;

public interface CourseService {
    List<CourseType> getAllCourse();

    ResultType addCourse(AddCourseInput input);

    Map<String, List<CourseType>> prerequisitesForCourses(List<String> courseIds);
}
