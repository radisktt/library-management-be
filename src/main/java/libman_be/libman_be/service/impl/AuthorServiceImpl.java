package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.AuthorDTO;
import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.entity.Author;
import libman_be.libman_be.exception.AuthorException;
import libman_be.libman_be.mapper.AuthorMapper;
import libman_be.libman_be.repository.AuthorRepository;
import libman_be.libman_be.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Override
    public BaseResponse<AuthorDTO> create(AuthorDTO dto) {
        if (authorRepository.findByName(dto.getName()) != null) {
            throw new AuthorException.AuthorAlreadyExistsException("Author with name " + dto.getName() + " already exists");
        }
        Author author = new Author();
        author.setName(dto.getName());
        Author saved = authorRepository.save(author);
        return BaseResponse.<AuthorDTO>builder()
                .status("success")
                .message("Author created successfully")
                .data(authorMapper.toDTO(saved))
                .build();
    }

    @Override
    public BaseResponse<AuthorDTO> update(Long id, AuthorDTO dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorException.AuthorNotFoundException("Author with id " + id + " not found"));

        if (dto.getName() != null && !dto.getName().equals(author.getName())) {
            if (authorRepository.findByName(dto.getName()) != null) {
                throw new AuthorException.AuthorAlreadyExistsException("Author with name " + dto.getName() + " already exists");
            }
            author.setName(dto.getName());
        }

        Author updated = authorRepository.save(author);
        return BaseResponse.<AuthorDTO>builder()
                .status("success")
                .message("Author updated successfully")
                .data(authorMapper.toDTO(updated))
                .build();
    }

    @Override
    public BaseResponse<String> delete(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorException.AuthorNotFoundException("Author with id " + id + " not found"));
        authorRepository.delete(author);
        return BaseResponse.<String>builder()
                .status("success")
                .message("Author deleted successfully")
                .data("Author with ID " + id + " has been deleted")
                .build();
    }

    @Override
    public BaseResponse<AuthorDTO> getById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorException.AuthorNotFoundException("Author with id " + id + " not found"));
        return BaseResponse.<AuthorDTO>builder()
                .status("success")
                .message("Author retrieved successfully")
                .data(authorMapper.toDTO(author))
                .build();
    }

    @Override
    public BaseResponse<List<AuthorDTO>> getAll() {
        List<AuthorDTO> authors = authorRepository.findAll()
                .stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
        return BaseResponse.<List<AuthorDTO>>builder()
                .status("success")
                .message("All authors retrieved successfully")
                .data(authors)
                .build();
    }
}
