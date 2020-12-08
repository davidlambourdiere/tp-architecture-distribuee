package com.episen.backend.repository;

import com.episen.backend.model.JobRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRequestRepository extends MongoRepository<JobRequest, String> {
}
