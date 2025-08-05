package libman_be.libman_be.service;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.AuthorDTO;

import java.util.List;

public interface AuthorService {
    BaseResponse<AuthorDTO> create(AuthorDTO dto);
    BaseResponse<AuthorDTO> update(Long id, AuthorDTO dto);
    BaseResponse<String> delete(Long id);
    BaseResponse<AuthorDTO> getById(Long id);
    BaseResponse<List<AuthorDTO>> getAll();
}
