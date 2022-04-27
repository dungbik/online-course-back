package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

import javax.persistence.*;

@Entity
@Table(name = "techs")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_id")
    private FileEntity logo;

    public static TechEntity makeTech(String name, FileEntity logo) {
        return TechEntity.builder()
                .name(name)
                .logo(logo)
                .build();
    }
}
