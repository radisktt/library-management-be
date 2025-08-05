package libman_be.libman_be.mapper;

import libman_be.libman_be.dto.LibraryDTO;
import libman_be.libman_be.entity.Library;
import org.springframework.stereotype.Component;

@Component
public class LibraryMapper {
    public LibraryDTO toDTO(Library library) {
        LibraryDTO dto = new LibraryDTO();
        dto.setId(library.getId());
        dto.setName(library.getName());
        dto.setLocation(library.getLocation());
        return dto;
    }

    public Library toEntity(LibraryDTO dto) {
        Library library = new Library();
        library.setId(dto.getId());
        library.setName(dto.getName());
        library.setLocation(dto.getLocation());
        return library;
    }
}
