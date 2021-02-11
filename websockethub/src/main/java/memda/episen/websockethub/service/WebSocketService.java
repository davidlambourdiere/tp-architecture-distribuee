package memda.episen.websockethub.service;

import memda.episen.dto.JobRequestDTO;
import memda.episen.dto.WebSocketDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {
    public ResponseEntity<String> createJobRequest(JobRequestDTO webSocketDTO) {
        return ResponseEntity.ok("YES");
    }
}
