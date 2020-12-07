package com.episen.frontend.model;

import com.episen.frontend.dto.JobRequestDTO;
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
    private String filename;
    public JobRequestDTO toJobRequestDTO(){
        return JobRequestDTO.builder()
                .id(id)
                .filename(filename)
                .build();
    }
}
