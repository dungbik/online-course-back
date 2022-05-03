package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.config.AWSConfig;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.*;
import yoonleeverse.onlinecourseback.modules.course.repository.*;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.*;
import yoonleeverse.onlinecourseback.modules.course.types.input.*;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;
import yoonleeverse.onlinecourseback.modules.file.repository.FileRepository;
import yoonleeverse.onlinecourseback.modules.file.service.StorageService;
import yoonleeverse.onlinecourseback.modules.payment.entity.PaymentEntity;
import yoonleeverse.onlinecourseback.modules.payment.repository.PaymentRepository;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import java.util.*;
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
    private final CommentRepository commentRepository;
    private final PaymentRepository paymentRepository;
    private final AWSConfig awsConfig;
    private final StorageService storageService;
    private final FileRepository fileRepository;

    public UserEntity currentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserEntity) context.getAuthentication().getPrincipal();
    }

    @Override
    public List<CourseType> getAllCourse() {
        return courseRepository.findAll().stream()
                .map((course) -> new CourseType(course, awsConfig.getFileCloudUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public CourseType getCourse(String slug) {
        CourseEntity exCourse = courseRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

        return new CourseType(exCourse, awsConfig.getFileCloudUrl());
    }

    @Override
    public ResultType addCourse(AddCourseInput input, MultipartFile file) {
        try {
            if (courseRepository.existsByTitle(input.getTitle()))
                throw new RuntimeException("이미 존재하는 이름입니다.");

            FileEntity logo = null;
            if (file != null) {
                String fileUrl = storageService.put(file, input.getTitle(), "public/course");
                logo = FileEntity.builder()
                        .fileUrl(fileUrl)
                        .build();
                fileRepository.save(logo);
            }

            CourseEntity course = CourseEntity.makeCourse(input, logo);
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
                        .course(course)
                        .title(videoInput.getTitle())
                        .time(videoInput.getTime())
                        .link(videoInput.getLink())
                        .freePreview(videoInput.getFreePreview())
                        .build();
                videoRepository.save(video);
            }));
        });
    }

    private void savePrerequisites(List<String> slugs, CourseEntity course) {
        courseRepository.getAllBySlugIn(slugs).stream()
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
    public Map<String, List<TechType>> techsForCourses(List<String> slugs) {
        Map<String, List<TechType>> result = new HashMap<>();
        slugs.forEach((id) -> result.put(id, new ArrayList<>()));

        courseTechRepository.findAllBySlugIn(slugs).stream()
                .forEach(obj ->
                    // todo mapper 클래스 만들어서 관리하는게 좋을듯
                    result.get((String) obj[0]).add(new TechType((TechEntity) obj[1], awsConfig.getFileCloudUrl()))
                );

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<CourseType>> prerequisitesForCourses(List<String> slugs) {
        Map<String, List<CourseType>> result = new HashMap<>();
        slugs.forEach((slug) -> result.put(slug, new ArrayList<>()));

        prerequisiteRepository.findAllBySlugIn(slugs).stream()
                .forEach(obj ->
                    result.get((String) obj[0]).add(new CourseType((CourseEntity) obj[1], awsConfig.getFileCloudUrl()))
                );

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<VideoCategoryType>> videoCategoriesForCourses(List<String> slugs) {
        Map<String, List<VideoCategoryType>> result = new HashMap<>();
        slugs.forEach((slug) -> result.put(slug, new ArrayList<>()));

        videoCategoryRepository.findAllBySlugIn(slugs).stream()
                .forEach(videoCategory ->
                        result.get(videoCategory.getCourse().getSlug()).add(new VideoCategoryType(videoCategory))
                );

        return result;
    }

    @Override
    public ResultType updateCourse(UpdateCourseInput input, MultipartFile file) {
        try {
            CourseEntity exCourse = courseRepository.findBySlug(input.getSlug())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

            FileEntity newLogo = null;
            if (file != null) {
                FileEntity oldLogo = exCourse.getLogo();
                if (oldLogo != null) {
                    storageService.delete(oldLogo.getFileUrl());
                }

                String fileUrl = storageService.put(file, input.getTitle(), "public/course");
                newLogo = FileEntity.builder()
                        .fileUrl(fileUrl)
                        .build();
                fileRepository.save(newLogo);
            }

            exCourse.updateCourse(input, newLogo);

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
        videoRepository.deleteAllByCategoryIn(videoCategoryRepository.findAllBySlug(course.getSlug()));
        videoCategoryRepository.deleteAllByCourse(course);
    }

    @Override
    public ResultType removeCourse(String slug) {
        try {
            CourseEntity exCourse = courseRepository.findBySlug(slug)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

            removeCourse(exCourse);

            FileEntity logo = exCourse.getLogo();
            if (logo != null) {
                storageService.delete(logo.getFileUrl());
            }

            courseRepository.delete(exCourse);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType addComment(AddCommentInput input) {
        try {
            VideoEntity exVideo = videoRepository.findByVideoId(input.getVideoId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 동영상입니다."));

            input.setWriter(currentUser());
            input.setVideo(exVideo);
            CommentEntity comment = CommentEntity.makeComment(input);

            commentRepository.save(comment);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType updateComment(UpdateCommentInput input) {
        try {
            CommentEntity exComment = commentRepository.findByCommentId(input.getCommentId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));

            if (exComment.getWriter() != currentUser())
                throw new RuntimeException("댓글 작성자가 아닙니다.");

            exComment.updateComment(input.getContent());

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType removeComment(String commentId) {
        try {
            CommentEntity exComment = commentRepository.findByCommentId(commentId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));

            if (exComment.getWriter() != currentUser())
                throw new RuntimeException("댓글 작성자가 아닙니다.");

            commentRepository.delete(exComment);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public List<CommentType> getAllComment(String videoId) {
        VideoEntity exVideo = videoRepository.findByVideoId(videoId)
                .orElse(null);

        if (exVideo == null)
            return new ArrayList<>();

        return commentRepository.findAllByVideo(exVideo).stream()
                .map(CommentType::new)
                .collect(Collectors.toList());
    }

    @Override
    public VideoType getVideo(String videoId) {
        VideoEntity exVideo = videoRepository.findByVideoId(videoId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 영상입니다."));

        Integer price = exVideo.getCourse().getPrice(); // todo null이 저장되지 않게 처리
        if (price == null || price > 0) {
            Optional<PaymentEntity> paymentHistory = paymentRepository.findByUserAndCourse(currentUser(), exVideo.getCourse());
            if (!paymentHistory.isPresent())
                throw new RuntimeException("구매하지 않은 강의입니다.");
        }

        return VideoType.of(exVideo);
    }

}
