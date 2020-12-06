package com.episen.frontend.service;

import com.episen.frontend.dto.JobRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobRequestService {
    private final AmqpTemplate amqpTemplate;

    @Value("${mem.rabbitmq.exchange}")
    private String RABBITMQ_EXCHANGE;

    @Value("${mem.rabbitmq.routingkey}")
    private String RABBITMQ_ROUTINGKEY;

    public ResponseEntity<Object> createJobRequest(JobRequestDTO jobRequest) {
        amqpTemplate.convertAndSend(RABBITMQ_EXCHANGE,RABBITMQ_ROUTINGKEY, jobRequest.getId());
        return ResponseEntity.ok(jobRequest);
    }
}
