package com.episen.backend.model;

import com.episen.backend.utils.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    @Id
    private String id;
    private Task task;
    private String filename;
}
