package yoonleeverse.onlinecourseback.modules.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.config.AWSConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

import static com.amazonaws.services.s3.model.CannedAccessControlList.PublicRead;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService implements StorageService {

    private final AWSConfig awsConfig;
    private final AmazonS3Client amazonS3Client;

    private String bucketName;

    @PostConstruct
    void init() {
        bucketName = awsConfig.getS3Bucket();
    }

    @Override
    public String put(MultipartFile file, String fileName, String filePath) {
        Assert.notNull(file, "업로드할 파일이 없습니다.");
        Assert.hasText(fileName, "파일 이름이 존재하지 않습니다.");
        Assert.hasText(fileName, "파일을 저장할 경로가 존재하지 않습니다.");

        String fileUrl = String.format("%s/%d-%s", filePath, System.currentTimeMillis() / 1000, fileName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            objectMetadata.setContentLength(inputStream.available());
            amazonS3Client
                    .putObject(new PutObjectRequest(bucketName, fileUrl, inputStream, objectMetadata)
                    .withCannedAcl(PublicRead));
        } catch (IOException e) {
            log.error(String.format("fileUrl: %s 파일 업로드 실패", fileUrl));
            throw new RuntimeException("파일 저장 실패");
        }

        return fileUrl;
    }

    @Override
    public void delete(String fileUrl) {
        Assert.hasText(fileUrl, "삭제할 파일 경로가 존재하지 않습니다.");

        try {
            amazonS3Client.deleteObject(bucketName, fileUrl);
        } catch (Exception e) {
            log.error(String.format("fileUrl: %s 파일 삭제 실패", fileUrl));
            throw new RuntimeException("파일 삭제 실패");
        }
    }


}
