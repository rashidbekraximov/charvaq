package uz.cluster.controllers.lb;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.lb.MixerDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.lb.MixerService;

import java.util.List;


@RestController
@RequestMapping("api/")
@Tag(name = "References Mixers", description = "Mikser")
@RequiredArgsConstructor
public class MixerController {

    private final MixerService mixerService;

    @GetMapping("mixers")
    public ResponseEntity<List<MixerDao>> getList() {
        return ResponseEntity.ok(mixerService.getList());
    }

    @GetMapping("mixer/select")
    public ResponseEntity<?> getSelectList() {
        return ResponseEntity.ok(mixerService.getHtmlList());
    }

    @GetMapping("mixer/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        MixerDao dao = mixerService.getById(id);
        if (dao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dao);
    }

    @PostMapping("mixer/save")
    public ResponseEntity<?> save(@RequestBody MixerDao dao) {
        ApiResponse apiResponse = mixerService.add(dao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("mixer/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = mixerService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
