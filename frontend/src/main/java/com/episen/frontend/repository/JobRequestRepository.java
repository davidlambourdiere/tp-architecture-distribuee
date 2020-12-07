package com.episen.frontend.repository;

import com.episen.frontend.model.JobRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRequestRepository extends MongoRepository<JobRequest, String> {
}
