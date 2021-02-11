package memda.episen.dataaccesslayer.repository;


import memda.episen.model.JobRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface JobRequestRepository extends MongoRepository<JobRequest, String> {
}
