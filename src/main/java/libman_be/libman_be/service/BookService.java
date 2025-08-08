package libman_be.libman_be.service;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookDTO;
import libman_be.libman_be.dto.response.BookResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    BaseResponse<BookDTO> create(BookDTO dto);

    BaseResponse<BookResponseDTO> update(Long id, BookDTO dto);

    BaseResponse<String> delete(Long id);

    BaseResponse<BookResponseDTO> getById(Long id);

    BaseResponse<Page<BookResponseDTO>> getAll(Pageable pageable);

    BaseResponse<Page<BookResponseDTO>> findByAuthorId(Long id, Pageable pageable);

    BaseResponse<Page<BookResponseDTO>> findByAuthorName(String name, Pageable pageable);

    BaseResponse<Page<BookResponseDTO>> findByCategoryId(Long categoryId, Pageable pageable);

    BaseResponse<Page<BookResponseDTO>> findByCategoryName(String categoryName, Pageable pageable);

    BaseResponse<Page<BookResponseDTO>> findByPublisherId(Long publisherId, Pageable pageable);

    BaseResponse<Page<BookResponseDTO>> findByPublisherName(String publisherName, Pageable pageable);
}