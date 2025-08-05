package libman_be.libman_be.service;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    BaseResponse<CategoryDTO> create(CategoryDTO dto);
    BaseResponse<CategoryDTO> getById(Long id);
    BaseResponse<List<CategoryDTO>> getAll();
    BaseResponse<CategoryDTO> update(Long id, CategoryDTO dto);
    BaseResponse<String> delete(Long id);
}
