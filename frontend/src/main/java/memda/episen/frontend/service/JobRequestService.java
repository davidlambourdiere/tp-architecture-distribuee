package memda.episen.frontend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memda.episen.dto.FileDTO;
import memda.episen.dto.JobRequestDTO;
import memda.episen.frontend.websocket.FrontendStompSessionHandler;
import memda.episen.model.JobRequest;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobRequestService {
    public static final String BUCKETNAME = "memdabucket";
    private final AmqpTemplate amqpTemplate;
    private  RestTemplate restTemplate = new RestTemplate();


    @Value("${mem.rabbitmq.exchange}")
    private String RABBITMQ_EXCHANGE;

    @Value("${mem.rabbitmq.routingkey}")
    private String RABBITMQ_ROUTINGKEY;

    private WebSocketStompClient stompClient;
    private StompSessionHandler sessionHandler;

    @PostConstruct
    public void init(){
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

        sendFileToAws(jobRequest.getFilename(), jobRequest.getText());
        log.info("Fichier {} crée", jobRequest.getFilename());
        amqpTemplate.convertAndSend(RABBITMQ_EXCHANGE,RABBITMQ_ROUTINGKEY, jobRequest.getId());
        return ResponseEntity.ok(jobRequest.toJobRequestDTO());
    }

/*    public ResponseEntity<FileDTO> getJobRequestResult(String jobrequestid) {
        JobRequest jobRequest = restTemplate.getForObject("localhost:8083/data/jobrequest/"+jobrequestid,JobRequest.class);
        return ResponseEntity.ok(restTemplate.getForObject("localhost:8084/files/"+jobRequest.getFilename(),FileDTO.class));
    }*/

    public void sendFileToAws(String filename, String content){
        FileDTO fileDTO = FileDTO.builder()
                .filename(filename)
                .content(content)
                .build();
        restTemplate.postForLocation("http://localhost:8084/files/", fileDTO);
    }


}
