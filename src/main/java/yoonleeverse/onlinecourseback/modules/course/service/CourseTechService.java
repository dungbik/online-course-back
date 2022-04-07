package yoonleeverse.onlinecourseback.modules.course.service;

import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.List;
import java.util.Map;

public interface CourseTechService {
    Map<String, List<TechType>> techsForCourses(List<String> courseIds);

}
