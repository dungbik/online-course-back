package yoonleeverse.onlinecourseback.modules.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/check")
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok().build();
    }
}
