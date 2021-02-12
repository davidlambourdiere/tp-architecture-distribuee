package memda.episen.fsaccesslayer.endpoints;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memda.episen.dto.FileDTO;
import memda.episen.fsaccesslayer.service.FsAccessService;
import memda.episen.model.JobRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(FsAccessController.PATH)
@RequiredArgsConstructor
public class FsAccessController {
    public static final String PATH = "/files";
    private final FsAccessService fsAccessService;

    @PostMapping
    public void createFile(@RequestBody FileDTO fileDTO){
        fsAccessService.sendFile(fileDTO);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<FileDTO> getFileByFilename(@PathVariable("filename") String filename){
        return fsAccessService.getFileByFilename(filename);
    }


}
