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

@Service
@Slf4j
@RequiredArgsConstructor
public class JobRequestService {
    private final AmqpTemplate amqpTemplate;
    private final JobRequestRepository jobRepository;

    @Value("${mem.rabbitmq.exchange}")
    private String RABBITMQ_EXCHANGE;

    @Value("${mem.rabbitmq.routingkey}")
    private String RABBITMQ_ROUTINGKEY;

    @PostConstruct
    public void init(){
        for (JobRequest jobRequest : jobRepository.findAll())
            log.debug(jobRequest.toString());
    }

    public ResponseEntity<Object> createJobRequest(JobRequestDTO jobRequestDTO) {
        JobRequest jobRequest = jobRepository.save(jobRequestDTO.toJobRequest());
        amqpTemplate.convertAndSend(RABBITMQ_EXCHANGE,RABBITMQ_ROUTINGKEY, jobRequest.getId());
        return ResponseEntity.ok(jobRequest.toJobRequestDTO());
    }
}
