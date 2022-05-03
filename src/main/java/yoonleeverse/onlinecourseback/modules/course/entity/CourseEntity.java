package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.*;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.common.utils.StringUtil;
import yoonleeverse.onlinecourseback.modules.course.types.input.UpdateCourseInput;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String slug;

    private String subTitle;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private FileEntity logo;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoCategoryEntity> videoCategories;

    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<PrerequisiteEntity> prerequisites;

    @OneToMany(mappedBy = "requiredCourse", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PrerequisiteEntity> followUps;

    private String mainColor;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    private Integer price;

    public void updateCourse(UpdateCourseInput input, List<VideoCategoryEntity> videoCategories, List<PrerequisiteEntity> prerequisites) {
        this.title = input.getTitle();
        this.slug = StringUtil.toSlug(input.getTitle());
        this.subTitle = input.getSubTitle();
        this.mainColor = input.getMainColor();
        this.level = LevelEnum.valueOf(input.getLevel());
        this.price = input.getPrice();
        this.videoCategories.forEach(category -> {
                    category.setParent(null);
                    category.getVideos()
                            .forEach(videoEntity -> videoEntity.setParent(category, null));
                }
        );
        this.getPrerequisites().forEach(prerequisite -> prerequisite.setParent(null));

        if (videoCategories != null) {
            videoCategories.forEach(category -> {
                category.setParent(this);
                category.getVideos()
                        .forEach(videoEntity -> videoEntity.setParent(category, this));
            });
            this.videoCategories.addAll(videoCategories);
        }
        if (prerequisites != null) {
            prerequisites.forEach(prerequisite -> prerequisite.setParent(this));
            this.prerequisites.addAll(prerequisites);
        }
    }
}
