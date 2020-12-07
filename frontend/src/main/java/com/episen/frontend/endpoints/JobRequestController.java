package com.episen.frontend.endpoints;

import com.episen.frontend.dto.JobRequestDTO;
import com.episen.frontend.service.JobRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
        log.info("Recu : {}", jobRequestDTO.toString());
        if (jobRequestDTO.getId() != null)
            return ResponseEntity.badRequest().body("id field must be null");
        return jobRequestService.createJobRequest(jobRequestDTO);
    }

}
