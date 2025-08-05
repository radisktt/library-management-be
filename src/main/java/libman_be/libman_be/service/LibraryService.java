package libman_be.libman_be.service;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.LibraryDTO;

import java.util.List;

public interface LibraryService {
    BaseResponse<LibraryDTO> create(LibraryDTO dto);
    BaseResponse<LibraryDTO> update(Long id, LibraryDTO dto);
    BaseResponse<String> delete(Long id);
    BaseResponse<LibraryDTO> getById(Long id);
    BaseResponse<List<LibraryDTO>> getAll();
}
