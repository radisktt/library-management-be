package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.PublisherDTO;
import libman_be.libman_be.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publisher")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<PublisherDTO>> create(@RequestBody PublisherDTO dto) {
        return ResponseEntity.ok(publisherService.create(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<PublisherDTO>> update(@PathVariable Long id, @RequestBody PublisherDTO dto) {
        return ResponseEntity.ok(publisherService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.delete(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PublisherDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getById(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<PublisherDTO>>> getAll() {
        return ResponseEntity.ok(publisherService.getAll());
    }
}
