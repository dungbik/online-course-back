package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddTechInput;

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

    @Lob @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private String logo;

    public static TechEntity makeTech(AddTechInput input) {
        return TechEntity.builder()
                .name(input.getName())
                .logo(input.getLogo())
                .build();
    }
}
