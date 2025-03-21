package com.example.bookstoreapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.entity.Category;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.CategoryMapper;
import com.example.bookstoreapp.repository.category.CategoryRepository;
import com.example.bookstoreapp.service.impl.CategoryServiceImpl;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;

    private Category category2;

    private CategoryResponseDto responseDto;

    private CategoryResponseDto responseDto2;

    private CategoryRequestDto requestDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Fiction");
        category.setDescription("Fictional books");
        category.setDeleted(false);

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Science");
        category2.setDescription("Science books");
        category2.setDeleted(false);

        responseDto = new CategoryResponseDto();
        responseDto.setName(category.getName());
        responseDto.setDescription(category.getDescription());
        responseDto.setId(category.getId());

        responseDto2 = new CategoryResponseDto();
        responseDto2.setName(category2.getName());
        responseDto2.setDescription(category2.getDescription());
        responseDto2.setId(category2.getId());

        requestDto = new CategoryRequestDto();
        requestDto.setName("Updated Fiction");
        requestDto.setDescription("Updated description");
    }

    @Test
    @DisplayName("Should return paginated list of categories")
    void findAll_WithValidPageable_ShouldReturnCategoryPage() {

        Pageable pageable = PageRequest.of(0, 2);

        when(categoryMapper.toDto(category)).thenReturn(responseDto);
        when(categoryMapper.toDto(category2)).thenReturn(responseDto2);

        List<Category> categories = List.of(category, category2);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);
        when(categoryMapper.toDto(category2)).thenReturn(responseDto2);

        Page<CategoryResponseDto> result = categoryService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(responseDto, responseDto2);

        verify(categoryRepository, Mockito.times(1)).findAll(pageable);
        verify(categoryMapper, Mockito.times(1)).toDto(category);
        verify(categoryMapper, Mockito.times(1)).toDto(category2);
    }

    @Test
    @DisplayName("Should return category DTO when category is found by ID")
    void getById_WithExistingId_ShouldReturnCategoryDto() {

        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getById(categoryId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(responseDto.getId());
        assertThat(result.getName()).isEqualTo(responseDto.getName());
        assertThat(result.getDescription()).isEqualTo(responseDto.getDescription());

        verify(categoryRepository, Mockito.times(1)).findById(categoryId);
        verify(categoryMapper, Mockito.times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when category is not found by ID")
    void getById_WithNonExistingId_ShouldThrowException() {

        Long categoryId = 99L;
        String expectedMessage = "can't find Category by id " + categoryId;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(categoryId));

        assertThat(exception.getMessage()).isEqualTo(expectedMessage);

        verify(categoryRepository, Mockito.times(1)).findById(categoryId);
        verify(categoryMapper, Mockito.never()).toDto(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should save category and return response DTO when valid request is provided")
    void save_WithValidRequest_ShouldReturnSavedCategoryDto() {

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.save(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Fiction", result.getName());
        assertEquals("Fictional books", result.getDescription());

        verify(categoryMapper, Mockito.times(1)).toModel(requestDto);
        verify(categoryRepository, Mockito.times(1)).save(category);
        verify(categoryMapper, Mockito.times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should throw ValidationException when category name is blank")
    void save_WithBlankCategoryName_ShouldThrowValidationException() {

        requestDto.setName("");

        String expectedMessage = "Category name cannot be blank";
        doThrow(new ValidationException(expectedMessage)).when(categoryMapper).toModel(requestDto);

        Exception exception = assertThrows(ValidationException.class,
                () -> categoryService.save(requestDto));

        assertEquals(expectedMessage, exception.getMessage());

        verify(categoryMapper, Mockito.times(1)).toModel(requestDto);
        verify(categoryRepository, Mockito.never()).save(ArgumentMatchers.any());
        verify(categoryMapper, Mockito.never()).toDto(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should update category and return updated DTO when category exists")
    void update_WithExistingId_ShouldUpdateAndReturnCategoryDto() {

        Long categoryId = 1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(requestDto, category);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.update(categoryId, requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(responseDto.getId());
        assertThat(result.getName()).isEqualTo(responseDto.getName());
        assertThat(result.getDescription()).isEqualTo(responseDto.getDescription());

        verify(categoryRepository, Mockito.times(1)).findById(categoryId);
        verify(categoryMapper, Mockito.times(1)).updateCategoryFromDto(requestDto, category);
        verify(categoryMapper, Mockito.times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating a non-existing category")
    void update_WithNonExistingId_ShouldThrowException() {

        Long categoryId = 99L;
        String expectedMessage = "can't find Category by id " + categoryId;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryId, requestDto));

        assertThat(exception.getMessage()).isEqualTo(expectedMessage);

        verify(categoryRepository, Mockito.times(1)).findById(categoryId);
        verify(categoryMapper, Mockito.never()).updateCategoryFromDto(ArgumentMatchers.any(),
                ArgumentMatchers.any());
        verify(categoryMapper, Mockito.never()).toDto(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should delete category when it exists")
    void deleteById_WithExistingId_ShouldDeleteCategory() {

        Long categoryId = 1L;

        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.deleteById(categoryId);

        verify(categoryRepository, Mockito.times(1)).existsById(categoryId);
        verify(categoryRepository, Mockito.times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when deleting a non-existing category")
    void deleteById_WithNonExistingId_ShouldThrowException() {

        Long categoryId = 99L;
        String expectedMessage = "can't find Category by id " + categoryId;

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deleteById(categoryId));

        assertThat(exception.getMessage()).isEqualTo(expectedMessage);

        verify(categoryRepository, Mockito.times(1)).existsById(categoryId);
        verify(categoryRepository, Mockito.never()).deleteById(ArgumentMatchers.any());
    }
}
