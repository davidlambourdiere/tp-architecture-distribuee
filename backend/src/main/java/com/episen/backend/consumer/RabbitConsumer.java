package com.episen.backend.consumer;

import com.episen.backend.model.JobRequest;
import com.episen.backend.repository.JobRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitConsumer {

    private final JobRequestRepository jobRequestRepository;

    @Value("${dla.jobfilepath}")
    private String jobfilepath;

    @RabbitListener(queues="${mem.rabbitmq.queue}")
    public void recievedMessage(String msg) throws IOException {
        log.info("Job request reçue: id {}", msg);
        Optional<JobRequest> optionalJobRequest = jobRequestRepository.findById(msg);
        if(optionalJobRequest.isPresent()) {
            File file = new File(String.format("%s/%s.txt", jobfilepath, optionalJobRequest.get().getId()));
            List<String> inputLines = Files.readAllLines(file.toPath());
            Files.write(file.toPath(),"".getBytes());
            inputLines.forEach(x -> {
                try { Files.write(file.toPath(),(x + "\n").toUpperCase().getBytes(), StandardOpenOption.APPEND);}
                catch (IOException e) {e.printStackTrace();}
            });
        }

    }
}
