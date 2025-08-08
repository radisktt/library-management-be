package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.CategoryDTO;
import libman_be.libman_be.entity.Category;
import libman_be.libman_be.exception.CategoryException;
import libman_be.libman_be.mapper.CategoryMapper;
import libman_be.libman_be.repository.CategoryRepository;
import libman_be.libman_be.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public BaseResponse<CategoryDTO> create(CategoryDTO dto) {
        if(categoryRepository.findByName(dto.getName()) != null){
            throw new CategoryException.CategoryAlreadyExistsException("Category with name " + dto.getName() + " already exists");
        }
        Category category = new Category();
        category.setName(dto.getName());
        CategoryDTO saved = categoryMapper.toDTO(categoryRepository.save(category));

        return BaseResponse.<CategoryDTO>builder()
                .status("success")
                .message("Category created successfully")
                .data(saved).build();
    }

    @Override
    public BaseResponse<CategoryDTO> update(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException.CategoryNotFoundException("Category with id " + id + " not found"));
        if (dto.getName() != null && !dto.getName().equals(category.getName())) {
            Category existedName = categoryRepository.findByName(dto.getName());
            if (existedName != null) {
                throw new CategoryException.CategoryAlreadyExistsException("Category with name " + dto.getName() + " already exists");
            }
            category.setName(dto.getName());
        }
        Category updatedCategory = categoryRepository.save(category);
        return BaseResponse.<CategoryDTO>builder()
                .status("success")
                .message("Category updated successfully")
                .data(categoryMapper.toDTO(updatedCategory))
                .build();
    }

    @Override
    public BaseResponse<String> delete(Long id) {
        Category deletedCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException.CategoryNotFoundException("Category with id " + id + " not found"));
        categoryRepository.delete(deletedCategory);
        return BaseResponse.<String>builder()
                .status("success")
                .message("Category deleted successfully")
                .data("Category with ID " + id + " has been deleted")
                .build();
    }

    @Override
    public BaseResponse<CategoryDTO> getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryException.CategoryNotFoundException("Category with id " + id + " not found")
        );
        return BaseResponse.<CategoryDTO>builder()
                .status("success")
                .message("Category retrieved successfully")
                .data(categoryMapper.toDTO(category))
                .build();
    }

    @Override
    public BaseResponse<List<CategoryDTO>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> response =  categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        return BaseResponse.<List<CategoryDTO>>builder()
                .status("success")
                .message("All categories retrieved successfully")
                .data(response).build();
    }
}
