package com.example.bookstoreapp.service.impl;

import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.entity.Category;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.CategoryMapper;
import com.example.bookstoreapp.repository.category.CategoryRepository;
import com.example.bookstoreapp.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CAN_T_FIND_CATEGORY_BY_ID_MSG = "can't find Category by id ";

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(()
                        -> new EntityNotFoundException(CAN_T_FIND_CATEGORY_BY_ID_MSG + id));
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto requestDto) {
        Category model = categoryMapper.toModel(requestDto);
        categoryRepository.save(model);
        return categoryMapper.toDto(model);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto requestDto) {
        Category category = categoryRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException(CAN_T_FIND_CATEGORY_BY_ID_MSG + id));
        Long categoryId = category.getId();
        categoryMapper.updateCategoryFromDto(requestDto, category);
        Category model = categoryMapper.toModel(requestDto);
        model.setId(categoryId);
        return categoryMapper.toDto(model);
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException(CAN_T_FIND_CATEGORY_BY_ID_MSG + id);
        }
        categoryRepository.deleteById(id);
    }
}
