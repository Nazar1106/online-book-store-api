package com.example.bookstoreapp.service;

import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import java.util.List;

public interface CategoryService {

    List<CategoryResponseDto> findAll();

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto requestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto requestDto);

    void deleteById(Long id);
}
