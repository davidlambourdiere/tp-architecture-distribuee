package memda.episen.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import memda.episen.dto.JobRequestDTO;
import memda.episen.model.JobRequest;
import memda.episen.repository.JobRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobRequestService {
    public static final String BUCKETNAME = "memdabucket";
    private final AmqpTemplate amqpTemplate;
    private final JobRequestRepository jobRequestRepository;

    @Value("${mem.rabbitmq.exchange}")
    private String RABBITMQ_EXCHANGE;

    @Value("${mem.rabbitmq.routingkey}")
    private String RABBITMQ_ROUTINGKEY;

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

        for(JobRequest jobRequest : jobRequestRepository.findAll()){
            log.info(jobRequest.toString());
            jobRequestRepository.delete(jobRequest);
        }
    }

    public ResponseEntity<Object> createJobRequest(JobRequestDTO jobRequestDTO) {
        JobRequest jobRequest = jobRequestRepository.save(jobRequestDTO.toJobRequest());
        sendFileToAws(jobRequest.getFilename(), new ByteArrayInputStream(jobRequest.getText().getBytes()));
        log.info("Fichier {} crée", jobRequest.getFilename());
        amqpTemplate.convertAndSend(RABBITMQ_EXCHANGE,RABBITMQ_ROUTINGKEY, jobRequest.getId());
        return ResponseEntity.ok(jobRequest.toJobRequestDTO());
    }

    public void sendFileToAws(String filename, InputStream inputStream){
        if (s3client.doesBucketExistV2(BUCKETNAME)){
            PutObjectResult putObjectResult= s3client.putObject(BUCKETNAME,filename, inputStream, new ObjectMetadata());
        }
    }
}
