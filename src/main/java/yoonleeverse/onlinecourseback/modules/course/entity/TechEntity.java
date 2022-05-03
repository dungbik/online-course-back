package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "techs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private FileEntity logo;

    @OneToMany(mappedBy = "tech", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CourseTechEntity> courses;
}
