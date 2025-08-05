package libman_be.libman_be.service;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookDTO;
import libman_be.libman_be.dto.response.BookResponseDTO;

import java.util.List;

public interface BookService {
    BaseResponse<BookDTO> create(BookDTO dto);

    BaseResponse<BookResponseDTO> update(Long id, BookDTO dto);

    BaseResponse<String> delete(Long id);

    BaseResponse<BookResponseDTO> getById(Long id);

    BaseResponse<List<BookResponseDTO>> getAll();
}