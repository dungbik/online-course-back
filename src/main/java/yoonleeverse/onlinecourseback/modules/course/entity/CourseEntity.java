package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.common.utils.StringUtil;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCourseInput;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCourseInput;

import javax.persistence.*;

@Entity
@Table(name = "courses")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String slug;

    private String subTitle;

    private String logo;

    private String mainColor;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    private Integer price;

    public static CourseEntity makeCourse(AddCourseInput input) {
        return CourseEntity.builder()
                .title(input.getTitle())
                .slug(StringUtil.toSlug(input.getTitle()))
                .subTitle(input.getSubTitle())
                .logo(input.getLogo())
                .mainColor(input.getMainColor())
                .level(LevelEnum.valueOf(input.getLevel()))
                .price(input.getPrice())
                .build();
    }

    public void updateCourse(UpdateCourseInput input) {
        this.title = input.getTitle();
        this.slug = StringUtil.toSlug(input.getTitle());
        this.subTitle = input.getSubTitle();
        this.logo = input.getLogo();
        this.mainColor = input.getMainColor();
        this.level = LevelEnum.valueOf(input.getLevel());
        this.price = input.getPrice();
    }
}
