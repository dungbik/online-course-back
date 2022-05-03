package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Builder;
import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;

@Data
@Builder
public class TechType {
    private Long id;
    private String name;
    private String logo;
}
