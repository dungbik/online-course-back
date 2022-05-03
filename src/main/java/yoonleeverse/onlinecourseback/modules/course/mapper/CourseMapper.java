package yoonleeverse.onlinecourseback.modules.course.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinecourseback.modules.common.utils.StringUtil;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.LevelEnum;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoCategoryEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.CategoryInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.VideoInput;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    public CourseEntity toEntity(AddCourseInput input) {
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
                .build();

        course.getVideoCategories().forEach((videoCategory) -> {
            videoCategory.setParent(course);
            videoCategory.getVideos()
                    .forEach((video) -> video.setParent(videoCategory, course));
        });
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

}
