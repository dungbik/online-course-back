package yoonleeverse.onlinecourseback.modules.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.config.StorageConfig;
import yoonleeverse.onlinecourseback.modules.common.constants.FileConstants;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalStorageService implements StorageService {

    private final StorageConfig storageConfig;

    private Path rootPath;

    @PostConstruct
    @Override
    public void init() {
        try {
            rootPath = Paths.get(storageConfig.getPath());
            Files.createDirectories(rootPath.resolve(FileConstants.TECH_PATH));
            Files.createDirectories(rootPath.resolve(FileConstants.COURSE_PATH));
            Files.createDirectories(rootPath.resolve(FileConstants.AVATAR_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @Override
    public String put(MultipartFile file, String fileName, String filePath) {
        Assert.notNull(file, "업로드할 파일이 없습니다.");
        Assert.hasText(fileName, "파일 이름이 존재하지 않습니다.");
        Assert.hasText(filePath, "파일을 저장할 경로가 존재하지 않습니다.");

        try {
            if (file == null)
                throw new Exception("Part is null");

            String ext = getExtension(file.getOriginalFilename()).orElse(null);
            if (ext == null)
                throw new Exception("file has not extension");

            String realName = UUID.randomUUID() + ext;
             Path path = Path.of(storageConfig.getPath(), filePath, realName);

            Files.createDirectories(path.getParent());

            file.transferTo(path);

            return Path.of(filePath, realName).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Path load(String filename) {
        return rootPath.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String fileUrl) {
        try {
            Path loadFile = load(fileUrl);

            return new UrlResource(loadFile.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Could not read file - fileUrl : " + fileUrl, e);
        }
    }

    @Override
    public void delete(String fileUrl) {

    }

    private Optional<String> getExtension(String fileName) {
        return Optional.ofNullable(fileName)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")));
    }
}
