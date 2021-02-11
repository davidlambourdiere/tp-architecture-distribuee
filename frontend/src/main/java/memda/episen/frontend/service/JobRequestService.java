package memda.episen.frontend.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import memda.episen.dto.JobRequestDTO;
import memda.episen.frontend.websocket.FrontendStompSessionHandler;
import memda.episen.model.JobRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.io.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobRequestService {
    public static final String BUCKETNAME = "memdabucket";
    private final AmqpTemplate amqpTemplate;
    private  RestTemplate restTemplate = new RestTemplate();


/*    @Value("${mem.rabbitmq.exchange}")
    private String RABBITMQ_EXCHANGE;

    @Value("${mem.rabbitmq.routingkey}")
    private String RABBITMQ_ROUTINGKEY;*/

    private AmazonS3 s3client;
    private WebSocketStompClient stompClient;
    private StompSessionHandler sessionHandler;

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

        //Création WebSocketStompClient
        StandardWebSocketClient webSocketClient = new StandardWebSocketClient();

        stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        sessionHandler = new FrontendStompSessionHandler();
    }

    public ResponseEntity<Object> createJobRequest(JobRequestDTO jobRequestDTO) {
        JobRequest jobRequest = restTemplate
                .postForObject("http://localhost:8083/data/jobrequest",jobRequestDTO,JobRequestDTO.class)
                .toJobRequest(); //data-access-layer

        stompClient
                .connect("ws://localhost:8082/websocket", sessionHandler)
                .addCallback(new ListenableFutureCallback<StompSession>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(StompSession stompSession) {
                log.info(stompSession.getSessionId());
                stompSession.send("/app/hello", new JobRequestDTO());
            }
        });

        //sendFileToAws(jobRequest.getFilename(), new ByteArrayInputStream(jobRequest.getText().getBytes()));
        log.info("Fichier {} crée", jobRequest.getFilename());
        //amqpTemplate.convertAndSend(RABBITMQ_EXCHANGE,RABBITMQ_ROUTINGKEY, jobRequest.getId());
        return ResponseEntity.ok(jobRequest.toJobRequestDTO());
    }

    public void sendFileToAws(String filename, InputStream inputStream){
        if (s3client.doesBucketExistV2(BUCKETNAME)){
            PutObjectResult putObjectResult= s3client.putObject(BUCKETNAME,filename, inputStream, new ObjectMetadata());
        }
    }
}
