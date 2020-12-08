package com.episen.frontend.service;

import com.episen.frontend.dto.JobRequestDTO;
import com.episen.frontend.model.JobRequest;
import com.episen.frontend.repository.JobRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobRequestService {
    private final AmqpTemplate amqpTemplate;
    private final JobRequestRepository jobRequestRepository;

    @Value("${mem.rabbitmq.exchange}")
    private String RABBITMQ_EXCHANGE;

    @Value("${mem.rabbitmq.routingkey}")
    private String RABBITMQ_ROUTINGKEY;

    @Value("${dla.jobfilepath}")
    private String jobfilepath;
    @PostConstruct
    public void init(){
        for(JobRequest jobRequest : jobRequestRepository.findAll()){
            File file = new File(String.format("%s/%s.txt", jobfilepath, jobRequest.getId()));
            file.delete();
            jobRequestRepository.delete(jobRequest);
        }
    }

    public ResponseEntity<Object> createJobRequest(JobRequestDTO jobRequestDTO) {
        JobRequest jobRequest = jobRequestRepository.save(jobRequestDTO.toJobRequest());
        File file = new File(String.format("%s/%s.txt", jobfilepath, jobRequest.getId()));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file,false);
            fileOutputStream.write(jobRequest.getText().getBytes());
            fileOutputStream.close();
            log.info("Fichier {} crée", file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        amqpTemplate.convertAndSend(RABBITMQ_EXCHANGE,RABBITMQ_ROUTINGKEY, jobRequest.getId());
        return ResponseEntity.ok(jobRequest.toJobRequestDTO());
    }
}
