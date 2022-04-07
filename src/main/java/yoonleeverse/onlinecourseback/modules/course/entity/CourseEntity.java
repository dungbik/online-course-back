package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.course.types.AddCourseInput;

import javax.persistence.*;
import java.util.UUID;

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

    @Column(unique = true, nullable = false)
    private String courseId;

    private String title;

    private String subTitle;

    private String logo;

    private String mainColor;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    private Integer price;

    public static CourseEntity makeCourse(AddCourseInput input) {
        return CourseEntity.builder()
                .courseId(UUID.randomUUID().toString())
                .title(input.getTitle())
                .subTitle(input.getSubTitle())
                .logo(input.getLogo())
                .mainColor(input.getMainColor())
                .level(LevelEnum.valueOf(input.getLevel()))
                .price(input.getPrice())
                .build();
    }
}
