package com.episen.backend.consumer;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memda.episen.dto.JobRequestDTO;
import memda.episen.model.JobRequest;
import memda.episen.utils.Task;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Optional;
import java.util.Scanner;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitConsumer {

    public static final String BUCKETNAME = "memdabucket";

    private RestTemplate restTemplate = new RestTemplate();

    private AmazonS3 s3client;

    @PostConstruct
    public void init(){
        AWSCredentials credentials = new BasicAWSCredentials(
                "AKIA43D56G7Z4VW5JBTH",
                "RmU4rg1vt5yKRJ0U4AcwVKR7oL6FFnsc4j7pcqpx"
        );
        s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.EU_WEST_3)
                .build();
    }
    @RabbitListener(queues="${mem.rabbitmq.queue}")
    public void recievedMessage(String msg) throws IOException {
        log.info("Job request id={}", msg);
        JobRequest jobRequest = restTemplate
                .getForObject("http://localhost:8083/data/jobrequest/{id}", JobRequest.class, msg);//Data Access Layer
        S3Object s3Object = getFileFromAws(jobRequest.getFilename());
        Scanner scanner = new Scanner(s3Object.getObjectContent());
        String output = "";
        while (scanner.hasNextLine()){
            String x = scanner.nextLine();
            if (jobRequest.getTask() == Task.UPPERCASE)
                x = x.toUpperCase();
            else if (jobRequest.getTask() == Task.LOWERCASE)
                x = x.toLowerCase();
            output += x + "\n";

            sendFileToAws(jobRequest.getFilename(),new ByteArrayInputStream(output.trim().getBytes()));

        }
    }
    public void sendFileToAws(String filename, InputStream inputStream){
        if (s3client.doesBucketExistV2(BUCKETNAME)){
            s3client.putObject(BUCKETNAME,filename, inputStream, new ObjectMetadata());
        }
    }
    public S3Object getFileFromAws(String filename){
        if (s3client.doesBucketExistV2(BUCKETNAME) && s3client.doesObjectExist(BUCKETNAME,filename)){
            return s3client.getObject(BUCKETNAME,filename);
        }
        return new S3Object();
    }
}
