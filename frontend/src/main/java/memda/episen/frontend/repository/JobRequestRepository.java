package memda.episen.frontend.repository;

import memda.episen.model.JobRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRequestRepository extends MongoRepository<JobRequest, String> {
}
