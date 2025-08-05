package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.LibraryDTO;
import libman_be.libman_be.entity.Library;
import libman_be.libman_be.exception.LibraryException;
import libman_be.libman_be.mapper.LibraryMapper;
import libman_be.libman_be.repository.LibraryRepository;
import libman_be.libman_be.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private LibraryMapper libraryMapper;

    @Override
    public BaseResponse<LibraryDTO> create(LibraryDTO dto) {
        if (libraryRepository.findByName(dto.getName()) != null) {
            throw new LibraryException.LibraryAlreadyExistsException("Library with name " + dto.getName() + " already exists");
        }
        Library library = new Library();
        library.setName(dto.getName());
        library.setLocation(dto.getLocation());
        LibraryDTO saved = libraryMapper.toDTO(libraryRepository.save(library));

        return BaseResponse.<LibraryDTO>builder()
                .status("success")
                .message("Library created successfully")
                .data(saved)
                .build();
    }

    @Override
    public BaseResponse<LibraryDTO> update(Long id, LibraryDTO dto) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryException.LibraryNotFoundException("Library with id " + id + " not found"));

        if (dto.getName() != null && !dto.getName().equals(library.getName())) {
            Library existed = libraryRepository.findByName(dto.getName());
            if (existed != null) {
                throw new LibraryException.LibraryAlreadyExistsException("Library with name " + dto.getName() + " already exists");
            }
            library.setName(dto.getName());
        }
        if (dto.getLocation() != null) {
            library.setLocation(dto.getLocation());
        }

        return BaseResponse.<LibraryDTO>builder()
                .status("success")
                .message("Library updated successfully")
                .data(libraryMapper.toDTO(libraryRepository.save(library)))
                .build();
    }

    @Override
    public BaseResponse<String> delete(Long id) {
        Library lib = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryException.LibraryNotFoundException("Library with id " + id + " not found"));
        libraryRepository.delete(lib);
        return BaseResponse.<String>builder()
                .status("success")
                .message("Library deleted successfully")
                .data("Library with ID " + id + " has been deleted")
                .build();
    }

    @Override
    public BaseResponse<LibraryDTO> getById(Long id) {
        Library lib = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryException.LibraryNotFoundException("Library with id " + id + " not found"));
        return BaseResponse.<LibraryDTO>builder()
                .status("success")
                .message("Library retrieved successfully")
                .data(libraryMapper.toDTO(lib))
                .build();
    }

    @Override
    public BaseResponse<List<LibraryDTO>> getAll() {
        List<Library> libs = libraryRepository.findAll();
        return BaseResponse.<List<LibraryDTO>>builder()
                .status("success")
                .message("All libraries retrieved successfully")
                .data(libs.stream().map(libraryMapper::toDTO).collect(Collectors.toList()))
                .build();
    }
}
