package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseTechEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.PrerequisiteEntity;
import yoonleeverse.onlinecourseback.modules.course.repository.CourseRepository;
import yoonleeverse.onlinecourseback.modules.course.repository.CourseTechRepository;
import yoonleeverse.onlinecourseback.modules.course.repository.PrerequisiteRepository;
import yoonleeverse.onlinecourseback.modules.course.repository.TechRepository;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseTechRepository courseTechRepository;
    private final TechRepository techRepository;
    private final PrerequisiteRepository prerequisiteRepository;

    @Override
    public List<CourseType> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(CourseType::new)
                .collect(Collectors.toList());
    }

    @Override
    public CourseType getCourse(String courseId) {
        return new CourseType(courseRepository.findByCourseId(courseId));
    }

    @Override
    public ResultType addCourse(AddCourseInput input) {
        try {
            if (courseRepository.existsByTitle(input.getTitle()))
                throw new RuntimeException("이미 존재하는 이름입니다.");

            CourseEntity course = CourseEntity.makeCourse(input);
            courseRepository.save(course);

            techRepository.findAllByIds(input.getMainTechs()).stream()
                    .forEach((tech) ->
                            courseTechRepository.save(CourseTechEntity.builder()
                                    .course(course)
                                    .tech(tech)
                                    .build())
                    );

            courseRepository.getAllByCourseIdIn(input.getPrerequisite()).stream()
                    .forEach((requiredCourse) ->
                        prerequisiteRepository.save(PrerequisiteEntity.builder()
                                .course(course)
                                .requiredCourse(requiredCourse)
                                .build())
                    );

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.toString());
        }
    }

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

    @Override
    public Map<String, List<CourseType>> prerequisitesForCourses(List<String> courseIds) {
        Map<String, List<CourseType>> result = new HashMap<>();
        courseIds.forEach((id) -> result.put(id, new ArrayList<>()));

        prerequisiteRepository.findAllByCourseIdIn(courseIds).stream()
                .forEach(obj ->
                    result.get((String) obj[0]).add(new CourseType((CourseEntity) obj[1]))
                );

        return result;
    }
}
