package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.*;
import yoonleeverse.onlinecourseback.modules.course.repository.*;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.*;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.CategoryInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCourseInput;

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
    private final VideoCategoryRepository videoCategoryRepository;
    private final VideoRepository videoRepository;

    @Override
    public List<CourseType> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(CourseType::new)
                .collect(Collectors.toList());
    }

    @Override
    public CourseType getCourse(String courseId) {
        CourseEntity exCourse = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

        return new CourseType(exCourse);
    }

    @Override
    public ResultType addCourse(AddCourseInput input) {
        try {
            if (courseRepository.existsByTitle(input.getTitle()))
                throw new RuntimeException("이미 존재하는 이름입니다.");

            CourseEntity course = CourseEntity.makeCourse(input);
            courseRepository.save(course);

            saveMainTechs(input.getMainTechs(), course);
            savePrerequisites(input.getPrerequisite(), course);
            saveVideoCategories(input.getVideoCategories(), course);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.toString());
        }
    }

    private void saveVideoCategories(List<CategoryInput> videoCategories, CourseEntity course) {
        videoCategories.stream().forEach((categoryInput) -> {
            VideoCategoryEntity category = VideoCategoryEntity.builder()
                    .course(course)
                    .title(categoryInput.getTitle())
                    .build();
            videoCategoryRepository.save(category);
            categoryInput.getVideos().stream().forEach((videoInput -> {
                VideoEntity video = VideoEntity.builder()
                        .category(category)
                        .title(videoInput.getTitle())
                        .time(videoInput.getTime())
                        .link(videoInput.getLink())
                        .build();
                videoRepository.save(video);
            }));
        });
    }

    private void savePrerequisites(List<String> ids, CourseEntity course) {
        courseRepository.getAllByCourseIdIn(ids).stream()
                .forEach((requiredCourse) ->
                    prerequisiteRepository.save(PrerequisiteEntity.builder()
                            .course(course)
                            .requiredCourse(requiredCourse)
                            .build())
                );
    }

    private void saveMainTechs(List<Long> mainTechs, CourseEntity course) {
        techRepository.findAllByIds(mainTechs).stream()
                .forEach((tech) ->
                        courseTechRepository.save(CourseTechEntity.builder()
                                .course(course)
                                .tech(tech)
                                .build())
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<TechType>> techsForCourses(List<String> courseIds) {
        Map<String, List<TechType>> result = new HashMap<>();
        courseIds.forEach((id) -> result.put(id, new ArrayList<>()));

        courseTechRepository.findAllByCourseIdIn(courseIds).stream()
                .forEach(obj ->
                    result.get((String) obj[0]).add(new TechType((TechEntity) obj[1]))
                );

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<CourseType>> prerequisitesForCourses(List<String> courseIds) {
        Map<String, List<CourseType>> result = new HashMap<>();
        courseIds.forEach((id) -> result.put(id, new ArrayList<>()));

        prerequisiteRepository.findAllByCourseIdIn(courseIds).stream()
                .forEach(obj ->
                    result.get((String) obj[0]).add(new CourseType((CourseEntity) obj[1]))
                );

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<VideoCategoryType>> videoCategoriesForCourses(List<String> courseIds) {
        Map<String, List<VideoCategoryType>> result = new HashMap<>();
        courseIds.forEach((id) -> result.put(id, new ArrayList<>()));

        videoCategoryRepository.findAllByCourseIdIn(courseIds).stream()
                .forEach(videoCategory ->
                        result.get(videoCategory.getCourse().getCourseId()).add(new VideoCategoryType(videoCategory))
                );

        return result;
    }

    @Override
    public ResultType updateCourse(UpdateCourseInput input) {
        try {
            CourseEntity exCourse = courseRepository.findByCourseId(input.getCourseId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

            exCourse.updateCourse(input);

            // todo 변경된 사항만 db가 수정되도록 (현재는 다 지우고 새로 넣음)
            removeCourse(exCourse);

            saveMainTechs(input.getMainTechs(), exCourse);
            savePrerequisites(input.getPrerequisite(), exCourse);
            saveVideoCategories(input.getVideoCategories(), exCourse);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    public void removeCourse(CourseEntity course) {
        courseTechRepository.deleteAllByCourse(course);
        prerequisiteRepository.deleteAllByCourse(course);
        videoRepository.deleteAllByCategoryIn(videoCategoryRepository.findAllByCourseId(course.getCourseId()));
        videoCategoryRepository.deleteAllByCourse(course);
    }

    @Override
    public ResultType removeCourse(String courseId) {
        try {
            CourseEntity exCourse = courseRepository.findByCourseId(courseId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

            removeCourse(exCourse);
            courseRepository.delete(exCourse);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

}
