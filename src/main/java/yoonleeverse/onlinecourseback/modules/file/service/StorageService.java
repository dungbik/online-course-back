package yoonleeverse.onlinecourseback.modules.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface StorageService {

    void init();

    String put(MultipartFile file, String fileName, String filePath);

    Path load(String filename);

    Resource loadAsResource(String fileUrl);

    void delete(String fileUrl);
}
