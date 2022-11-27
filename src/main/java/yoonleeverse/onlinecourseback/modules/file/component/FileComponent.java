package yoonleeverse.onlinecourseback.modules.file.component;

import org.springframework.stereotype.Component;
import yoonleeverse.onlinecourseback.modules.common.constants.FileConstants;

import java.nio.file.Path;

@Component
public class FileComponent {

    public String getFileUrl(String filePath) {
        return Path.of(FileConstants.IMAGE_ENDPOINT, filePath).toString();
    }
}
