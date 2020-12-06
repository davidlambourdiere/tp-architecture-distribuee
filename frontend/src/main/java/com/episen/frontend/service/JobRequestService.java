package com.episen.frontend.service;

import com.episen.frontend.dto.JobRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobRequestService {
    private static String QUEUE_NAME;
    public ResponseEntity<JobRequestDTO> createJobRequest(JobRequestDTO jobRequestDTO) {
        log.info(JobRequestService.QUEUE_NAME);
        return ResponseEntity.ok(jobRequestDTO);
    }
}
