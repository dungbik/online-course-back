package yoonleeverse.onlinecourseback.modules.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
