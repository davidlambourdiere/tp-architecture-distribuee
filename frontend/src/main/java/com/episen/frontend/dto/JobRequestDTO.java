package com.episen.frontend.dto;

import com.episen.frontend.utils.Task;
import com.episen.frontend.model.JobRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobRequestDTO {
    private String id;
    private Task task;
    private String filename;
    private String text;
    public JobRequest toJobRequest(){
        return JobRequest.builder()
                .id(id)
                .task(task)
                .filename(filename)
                .text(text)
                .build();
    }
}
