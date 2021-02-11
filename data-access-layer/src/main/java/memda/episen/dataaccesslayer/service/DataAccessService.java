package memda.episen.dataaccesslayer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memda.episen.dataaccesslayer.repository.JobRequestRepository;
import memda.episen.dto.JobRequestDTO;
import memda.episen.model.JobRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataAccessService {
    private final JobRequestRepository jobRequestRepository;
    @PostConstruct
    public void init(){
        for(JobRequest jobRequest : jobRequestRepository.findAll()){
            log.info(jobRequest.toString());
            jobRequestRepository.delete(jobRequest);
        }
    }
    public ResponseEntity<JobRequestDTO> createJobRequest(JobRequestDTO jobRequestDTO) {
        JobRequest jobRequest = jobRequestRepository.save(jobRequestDTO.toJobRequest());
        log.info("jobRequest crée : {}", jobRequest.toString());
        return ResponseEntity.ok(jobRequest.toJobRequestDTO());
    }

    public ResponseEntity<JobRequest> getJobRequestById(String jobRequestId) {
        Optional<JobRequest> optionalJobRequest = jobRequestRepository.findById(jobRequestId);
        return ResponseEntity.ok(optionalJobRequest.orElse(new JobRequest()));
    }
}
