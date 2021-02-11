package memda.episen.websockethub.endpoints;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memda.episen.dto.JobRequestDTO;
import memda.episen.dto.WebSocketDTO;
import memda.episen.websockethub.service.WebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(WebSocketController.PATH)
@CrossOrigin("*")
@Slf4j
public class WebSocketController {
    public static final String PATH = "";
    private final WebSocketService webSocketService;

    @MessageMapping("/hello")
    @SendToUser("/queue/notification")
    public String createWebSocket(@RequestBody JobRequestDTO jobRequestDTO){
        log.info(jobRequestDTO.toString());
        if (jobRequestDTO.getId() != null)
            return ResponseEntity.badRequest().body("id field must be null").getBody();
        return webSocketService.createJobRequest(jobRequestDTO).getBody();
    }
}
