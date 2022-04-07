package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseTechEntity;
import yoonleeverse.onlinecourseback.modules.course.repository.CourseTechRepository;
import yoonleeverse.onlinecourseback.modules.course.service.CourseTechService;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseTechServiceImpl implements CourseTechService {

    private final CourseTechRepository courseTechRepository;

    @Override
    public Map<String, List<TechType>> techsForCourses(List<String> courseIds) {
        return courseIds.stream().map(courseTechRepository::findByCourseId)
                .collect(Collectors.toMap(
                        i1 -> i1.get(0).getCourse().getCourseId(),
                        i2 -> i2.stream()
                                .map((ct) -> new TechType(ct.getTech()))
                                .collect(Collectors.toList()))
                );
    }
}
