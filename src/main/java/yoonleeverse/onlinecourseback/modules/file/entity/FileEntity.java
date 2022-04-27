package yoonleeverse.onlinecourseback.modules.file.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "files")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String fileUrl;

    public void updateFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
