package yoonleeverse.onlinecourseback.modules.file.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

@Component
@RequiredArgsConstructor
public class FileMapper {

    public FileEntity toEntity(String fileUrl) {
        return FileEntity.builder()
                .fileUrl(fileUrl)
                .build();
    }
}
