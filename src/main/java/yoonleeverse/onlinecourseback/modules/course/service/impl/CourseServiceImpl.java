package yoonleeverse.onlinecourseback.modules.course.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.*;
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
    private final VideoRepository videoRepository;
    private final CommentRepository commentRepository;
    private final StorageService storageService;
    private final CourseMapper courseMapper;
    private final FileMapper fileMapper;
    private final VideoHistoryRepository videoHistoryRepository;
    private final CourseEnrollmentRepository enrollmentRepository;

    public UserEntity currentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        try {
            return (UserEntity) context.getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<CourseType> getAllCourse() {
        List<CourseType> courseDTOList =  courseRepository.findAll().stream()
                .map(courseMapper::toDTO)
                .collect(Collectors.toList());

        UserEntity user = currentUser();
        if (user != null) {
            List<Long> completedVideoIds = videoHistoryRepository.findVideoIds(user);
            List<String> enrolledSlugs = enrollmentRepository.findCourseIds(user);
            for (CourseType course : courseDTOList) {
                int progress = 0;
                for (VideoCategoryType category : course.getVideoCategories()) {
                    for (VideoType video : category.getVideos()) {
                        if (completedVideoIds.contains(video.getVideoId())) {
                            video.setIsCompleted(true);
                            progress++;
                        }
                    }
                }
                course.setProgress(progress);
                if (enrolledSlugs.contains(course.getSlug())) {
                    course.setIsEnrolled(true);
                }
            }

        }

        return courseDTOList;
    }

    @Override
    public CourseType getCourse(String slug) {
        CourseEntity exCourse = courseRepository.findWithVideosBySlug(slug)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

        CourseType courseDTO = courseMapper.toDTO(exCourse);
        UserEntity user = currentUser();
        if (user != null) {
            List<Long> completedVideoIds = videoHistoryRepository.findVideoIdsByCourse(user, exCourse);
            List<String> enrolledSlugs = enrollmentRepository.findCourseIdsByCourse(user, exCourse);

            courseDTO.getVideoCategories().forEach((category) ->
                    category.getVideos().forEach((video) -> {
                        if (completedVideoIds.contains(video.getVideoId())) {
                            video.setIsCompleted(true);
                        }
                    }));
            if (enrolledSlugs.contains(courseDTO.getSlug())) {
                courseDTO.setIsEnrolled(true);
            }
        }

        return courseDTO;
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

        UserEntity user = currentUser();
        if (user == null)
            throw new RuntimeException("유저 정보가 존재하지 않습니다.");

        enrollmentRepository.findByUserAndCourse(user, exVideo.getCourse())
                .orElseThrow(() -> new RuntimeException("등록되지 않은 강의입니다."));

        return courseMapper.toDTO(exVideo);
    }

    @Override
    public ResultType completeVideo(Long videoId) {
        try {
            VideoEntity exVideo = videoRepository.findById(videoId)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 영상입니다."));

            UserEntity user = currentUser();
            if (user == null)
                throw new RuntimeException("유저 정보가 존재하지 않습니다.");

            enrollmentRepository.findByUserAndCourse(user, exVideo.getCourse())
                    .orElseThrow(() -> new RuntimeException("등록되지 않은 강의입니다."));

            VideoHistoryEntity exVideoHistory
                    = videoHistoryRepository.findByUserAndVideo(user, exVideo).orElse(null);

            if (exVideoHistory == null) {
                VideoHistoryEntity videoHistory = VideoHistoryEntity.builder()
                        .user(user).video(exVideo).build();
                videoHistoryRepository.save(videoHistory);
            } else {
                videoHistoryRepository.delete(exVideoHistory);
            }

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType enroll(String slug) {
        try {
            CourseEntity exCourse = courseRepository.findBySlug(slug)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 강의입니다."));

            UserEntity user = currentUser();
            if (user == null)
                throw new RuntimeException("유저 정보가 존재하지 않습니다.");

            if (enrollmentRepository.findByUserAndCourse(user, exCourse).isPresent())
                throw new RuntimeException("이미 등록된 강의입니다.");

            if (exCourse.getPrice() > 0)
                throw new RuntimeException("유료 강의는 결제가 필요합니다.");

            CourseEnrollmentEntity enrollment = CourseEnrollmentEntity.builder()
                    .user(user).course(exCourse).build();
            enrollmentRepository.save(enrollment);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

}
