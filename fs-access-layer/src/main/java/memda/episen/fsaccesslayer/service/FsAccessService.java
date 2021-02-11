package memda.episen.fsaccesslayer.service;

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
import memda.episen.dto.FileDTO;
import memda.episen.model.JobRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
@Slf4j
public class FsAccessService {

    public static final String BUCKETNAME = "memdabucket";

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

    public void sendFile(FileDTO fileDTO) {
        if (s3client.doesBucketExistV2(BUCKETNAME)){
            InputStream inputStream = new ByteArrayInputStream(fileDTO.content.getBytes());
            s3client.putObject(BUCKETNAME,fileDTO.filename, inputStream, new ObjectMetadata());
        }
    }

    public ResponseEntity<FileDTO> getFileByFilename(String filename) {
        log.info(filename);
        if (s3client.doesBucketExistV2(BUCKETNAME) && s3client.doesObjectExist(BUCKETNAME,filename)){
            log.info(filename);
            S3Object s3Object= s3client.getObject(BUCKETNAME,filename);
            Scanner scanner = new Scanner(s3Object.getObjectContent());
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) stringBuilder.append(scanner.nextLine());

            return ResponseEntity.ok(FileDTO.builder()
                    .filename(filename)
                    .content(stringBuilder.toString())
                    .build());
        }
        return ResponseEntity.ok(FileDTO.builder().filename(filename).build());
    }
}

