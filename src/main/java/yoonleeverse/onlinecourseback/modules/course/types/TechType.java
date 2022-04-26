package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;

@Data
public class TechType {
    private Long id;
    private String name;
    private String logo;

    public TechType(TechEntity tech, String cloudUrl) {
        this.id = tech.getId();
        this.name = tech.getName();
        this.logo = String.format("%s/%s", cloudUrl, tech.getLogo().getFileUrl());
    }
}
