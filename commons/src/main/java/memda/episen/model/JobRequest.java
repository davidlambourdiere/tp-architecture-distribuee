package memda.episen.model;

import memda.episen.dto.JobRequestDTO;
import memda.episen.utils.Task;
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
    private String text;
    public JobRequestDTO toJobRequestDTO(){
        return JobRequestDTO.builder()
                .id(id)
                .task(task)
                .filename(filename)
                .text(text)
                .build();
    }
}
