package libman_be.libman_be.service;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.PublisherDTO;

import java.util.List;

public interface PublisherService {
    BaseResponse<PublisherDTO> create(PublisherDTO dto);
    BaseResponse<PublisherDTO> update(Long id, PublisherDTO dto);
    BaseResponse<String> delete(Long id);
    BaseResponse<PublisherDTO> getById(Long id);
    BaseResponse<List<PublisherDTO>> getAll();
}
