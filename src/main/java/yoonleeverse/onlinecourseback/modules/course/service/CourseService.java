package yoonleeverse.onlinecourseback.modules.course.service;

import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.types.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;

import java.util.List;

public interface CourseService {
    List<CourseType> getAllCourse();

    ResultType addCourse(AddCourseInput input);

}
