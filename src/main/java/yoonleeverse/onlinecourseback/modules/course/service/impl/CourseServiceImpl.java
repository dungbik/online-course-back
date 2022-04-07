package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseTechEntity;
import yoonleeverse.onlinecourseback.modules.course.repository.CourseRepository;
import yoonleeverse.onlinecourseback.modules.course.repository.CourseTechRepository;
import yoonleeverse.onlinecourseback.modules.course.repository.TechRepository;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseTechRepository courseTechRepository;
    private final TechRepository techRepository;

    @Override
    public List<CourseType> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(CourseType::new)
                .collect(Collectors.toList());
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

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.toString());
        }
    }
}
