package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.CommentEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;
import yoonleeverse.onlinecourseback.modules.course.mapper.CourseMapper;
import yoonleeverse.onlinecourseback.modules.course.repository.*;
import yoonleeverse.onlinecourseback.modules.course.service.CourseService;
import yoonleeverse.onlinecourseback.modules.course.types.*;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCommentInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCommentInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCourseInput;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;
import yoonleeverse.onlinecourseback.modules.file.mapper.FileMapper;
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
    private final StorageService storageService;
    private final CourseMapper courseMapper;
    private final FileMapper fileMapper;

    public UserEntity currentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserEntity) context.getAuthentication().getPrincipal();
    }

    @Override
    public List<CourseType> getAllCourse() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CourseType getCourse(String slug) {
        CourseEntity exCourse = courseRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

        return courseMapper.toDTO(exCourse);
    }

    @Override
    public ResultType addCourse(AddCourseInput input, MultipartFile file) {
        try {
            if (courseRepository.existsByTitle(input.getTitle()))
                throw new RuntimeException("이미 존재하는 이름입니다.");

            if (file != null) {
                String fileUrl = storageService.put(file, input.getTitle(), "public/course");
                input.setLogo(fileMapper.toEntity(fileUrl));
            }

            CourseEntity course = courseMapper.toEntity(
                    input, courseRepository.getAllBySlugIn(input.getPrerequisite()),
                    techRepository.findAllByIds(input.getMainTechs()));
            courseRepository.save(course);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.toString());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<TechType>> techsForCourses(List<String> slugs) {
        Map<String, List<TechType>> result = new HashMap<>();
        slugs.forEach((id) -> result.put(id, new ArrayList<>()));

        courseTechRepository.findAllBySlugIn(slugs).stream()
                .forEach(obj -> result.get((String) obj[0]).add(courseMapper.toDTO((TechEntity) obj[1])));

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<CourseType>> prerequisitesForCourses(List<String> slugs) {
        Map<String, List<CourseType>> result = new HashMap<>();
        slugs.forEach((slug) -> result.put(slug, new ArrayList<>()));

        prerequisiteRepository.findAllBySlugIn(slugs).stream()
                .forEach(obj ->
                    result.get((String) obj[0]).add(courseMapper.toDTO((CourseEntity) obj[1]))
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

            if (file != null) {
                FileEntity oldLogo = exCourse.getLogo();
                if (oldLogo != null) {
                    storageService.delete(oldLogo.getFileUrl());
                }

                String fileUrl = storageService.put(file, input.getTitle(), "public/course");
                exCourse.getLogo().updateFileUrl(fileUrl);
            }

            exCourse.updateCourse(
                    input,
                    input.getVideoCategories().stream()
                            .map(courseMapper::toEntity).collect(Collectors.toList()),
                    courseRepository.getAllBySlugIn(input.getPrerequisite()).stream()
                            .map(courseMapper::toEntity).collect(Collectors.toList()),
                    techRepository.findAllByIds(input.getMainTechs()).stream()
                            .map(courseMapper::toEntity).collect(Collectors.toList())
                    );

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType removeCourse(String slug) {
        try {
            CourseEntity exCourse = courseRepository.findBySlug(slug)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

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
            VideoEntity exVideo = videoRepository.findById(input.getVideoId())
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
    public List<CommentType> getAllComment(Long videoId) {
        VideoEntity exVideo = videoRepository.findById(videoId)
                .orElse(null);

        if (exVideo == null)
            return new ArrayList<>();

        return commentRepository.findAllByVideo(exVideo).stream()
                .map(CommentType::new)
                .collect(Collectors.toList());
    }

    @Override
    public VideoType getVideo(Long videoId) {
        VideoEntity exVideo = videoRepository.findById(videoId)
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
