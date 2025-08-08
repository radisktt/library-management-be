package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.LibraryDTO;
import libman_be.libman_be.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<LibraryDTO>> create(@RequestBody LibraryDTO dto) {
        return ResponseEntity.ok(libraryService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<LibraryDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.getById(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<LibraryDTO>>> getAll() {
        return ResponseEntity.ok(libraryService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<LibraryDTO>> update(@PathVariable Long id, @RequestBody LibraryDTO dto) {
        return ResponseEntity.ok(libraryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.delete(id));
    }
}
