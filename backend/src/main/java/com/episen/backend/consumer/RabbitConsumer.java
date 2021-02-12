package com.episen.backend.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memda.episen.dto.FileDTO;
import memda.episen.model.JobRequest;
import memda.episen.utils.Task;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitConsumer {

    public static final String BUCKETNAME = "memdabucket";

    private RestTemplate restTemplate = new RestTemplate();

    @RabbitListener(queues="${mem.rabbitmq.queue}")
    public void recievedMessage(String msg) throws IOException {
        log.info("Job request id={}", msg);
        JobRequest jobRequest = restTemplate
                .getForObject("http://localhost:8083/data/jobrequest/{id}", JobRequest.class, msg);//Data Access Layer
        FileDTO fileDTO = getFileFromAws(jobRequest.getFilename());
        if (jobRequest.getTask() == Task.UPPERCASE)
            fileDTO.setContent(fileDTO.content.toUpperCase());
        else if (jobRequest.getTask() == Task.LOWERCASE)
            fileDTO.setContent(fileDTO.content.toLowerCase());
        sendFileToAws(fileDTO);
    }
    public void sendFileToAws(FileDTO fileDTO){
        restTemplate.postForLocation("http://localhost:8084/files/", fileDTO);
    }
    public FileDTO getFileFromAws(String filename){
        return restTemplate.getForObject("http://localhost:8084/files/"+filename,FileDTO.class);
    }
}
