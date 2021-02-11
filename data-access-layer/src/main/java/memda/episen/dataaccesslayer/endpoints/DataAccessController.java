package memda.episen.dataaccesslayer.endpoints;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memda.episen.dataaccesslayer.service.DataAccessService;
import memda.episen.dto.JobRequestDTO;
import memda.episen.model.JobRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(DataAccessController.PATH)
@RequiredArgsConstructor
public class DataAccessController {
    public static final String PATH = "/data/";
    private final DataAccessService dataAccessService;

    @PostMapping("/jobrequest")
    public ResponseEntity<JobRequestDTO> createJobRequest(@RequestBody JobRequestDTO jobRequestDTO){
        return dataAccessService.createJobRequest(jobRequestDTO);
    }

    @GetMapping("/jobrequest/{id}")
    public ResponseEntity<JobRequest> getJobRequestById(@PathVariable("id") String jobRequestId){
        return dataAccessService.getJobRequestById(jobRequestId);
    }


}
