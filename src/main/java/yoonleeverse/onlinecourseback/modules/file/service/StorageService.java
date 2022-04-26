package yoonleeverse.onlinecourseback.modules.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String put(MultipartFile file, String fileName, String filePath);

    void delete(String fileUrl);
}
