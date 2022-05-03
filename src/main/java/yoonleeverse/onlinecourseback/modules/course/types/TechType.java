package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechType {
    private Long id;
    private String name;
    private String logo;
}
