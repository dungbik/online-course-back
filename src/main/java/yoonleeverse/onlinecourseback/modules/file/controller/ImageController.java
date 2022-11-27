package yoonleeverse.onlinecourseback.modules.file.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yoonleeverse.onlinecourseback.modules.file.service.LocalStorageService;

import java.io.IOException;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final LocalStorageService localStorageService;

    @GetMapping("/{type}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String type, @PathVariable String filename) {

        Resource resource = localStorageService.loadAsResource(type + "/" + filename);

        if (!resource.exists())
            return ResponseEntity.notFound().build();

        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(contentType))
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
