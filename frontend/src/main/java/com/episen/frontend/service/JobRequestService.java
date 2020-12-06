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
    private static String RABBITMQ_EXCHANGE;

    @Value("${mem.rabbitmq.routingkey}")
    private static String RABBITMQ_ROUTINGKEY;

    public ResponseEntity<JobRequestDTO> createJobRequest(JobRequestDTO jobRequestDTO) {
        amqpTemplate.convertAndSend(RABBITMQ_EXCHANGE,RABBITMQ_ROUTINGKEY,jobRequestDTO.getId());
        return ResponseEntity.ok(jobRequestDTO);
    }
}
