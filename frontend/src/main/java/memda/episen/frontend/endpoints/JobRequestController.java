package memda.episen.frontend.endpoints;

import memda.episen.dto.JobRequestDTO;
import memda.episen.frontend.service.JobRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(JobRequestController.PATH)
@RequiredArgsConstructor
public class JobRequestController {
    public static final String PATH = "/jobrequest/";
    private final JobRequestService jobRequestService;

    @PostMapping
    public ResponseEntity<Object> createJobRequest(@RequestBody JobRequestDTO jobRequestDTO){
        if (jobRequestDTO.getId() != null)
            return ResponseEntity.badRequest().body("id field must be null");
        return jobRequestService.createJobRequest(jobRequestDTO);
    }

}
