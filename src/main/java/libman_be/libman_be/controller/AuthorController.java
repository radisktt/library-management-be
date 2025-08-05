package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.AuthorDTO;
import libman_be.libman_be.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<BaseResponse<AuthorDTO>> create(@RequestBody AuthorDTO dto) {
        return ResponseEntity.ok(authorService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<AuthorDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getById(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<AuthorDTO>>> getAll() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<AuthorDTO>> update(@PathVariable Long id, @RequestBody AuthorDTO dto) {
        return ResponseEntity.ok(authorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.delete(id));
    }
}
