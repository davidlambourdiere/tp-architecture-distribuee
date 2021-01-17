package memda.episen.repository;

import memda.episen.model.JobRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRequestRepository extends MongoRepository<JobRequest, String> {
}
