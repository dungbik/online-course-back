package yoonleeverse.onlinecourseback.modules.course.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinecourseback.config.AWSConfig;
import yoonleeverse.onlinecourseback.modules.common.utils.StringUtil;
import yoonleeverse.onlinecourseback.modules.course.entity.*;
import yoonleeverse.onlinecourseback.modules.course.types.TechType;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.CategoryInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.VideoInput;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final AWSConfig awsConfig;

    public CourseEntity toEntity(AddCourseInput input, List<CourseEntity> prerequisites, List<TechEntity> mainTechs) {
        CourseEntity course = CourseEntity.builder()
                .title(input.getTitle())
                .slug(StringUtil.toSlug(input.getTitle()))
                .subTitle(input.getSubTitle())
                .logo(input.getLogo())
                .mainColor(input.getMainColor())
                .level(LevelEnum.valueOf(input.getLevel()))
                .price(input.getPrice())
                .videoCategories(input.getVideoCategories().stream()
                        .map((category) -> toEntity(category)).collect(Collectors.toList()))
                .prerequisites(prerequisites.stream()
                        .map((prerequisite) -> toEntity(prerequisite)).collect(Collectors.toList()))
                .mainTechs(mainTechs.stream()
                        .map((tech) -> toEntity(tech)).collect(Collectors.toList()))
                .build();

        course.getVideoCategories().forEach((videoCategory) -> {
            videoCategory.setParent(course);
            videoCategory.getVideos()
                    .forEach((video) -> video.setParent(videoCategory, course));
        });
        course.getPrerequisites().forEach((prerequisite -> prerequisite.setParent(course)));
        course.getMainTechs().forEach((tech) -> tech.setParent(course));
        return course;
    }

    public VideoCategoryEntity toEntity(CategoryInput input) {
        return VideoCategoryEntity.builder()
                .title(input.getTitle())
                .videos(input.getVideos().stream().map((video) -> toEntity(video)).collect(Collectors.toList()))
                .build();
    }

    public VideoEntity toEntity(VideoInput input) {
        return VideoEntity.builder()
                .title(input.getTitle())
                .time(input.getTime())
                .link(input.getLink())
                .freePreview(input.getFreePreview())
                .text(input.getText())
                .build();
    }

    public PrerequisiteEntity toEntity(CourseEntity requiredCourse) {
        return PrerequisiteEntity.builder()
                .requiredCourse(requiredCourse)
                .build();
    }

    public CourseTechEntity toEntity(TechEntity tech) {
        return CourseTechEntity.builder()
                .tech(tech)
                .build();
    }

    public TechEntity toEntity(String name, FileEntity logo) {
        return TechEntity.builder()
                .name(name)
                .logo(logo)
                .build();
    }

    public TechType toDTO(TechEntity tech) {
        String logo = null;
        if (tech.getLogo() != null) {
            logo = String.format("%s/%s", awsConfig.getFileCloudUrl(), tech.getLogo().getFileUrl());
        }

        return TechType.builder()
                .id(tech.getId()).name(tech.getName()).logo(logo)
                .build();
    }
}
