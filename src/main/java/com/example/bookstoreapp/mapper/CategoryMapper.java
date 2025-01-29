package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.categorydto.CategoryRequestDto;
import com.example.bookstoreapp.dto.categorydto.CategoryResponseDto;
import com.example.bookstoreapp.entity.Category;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryResponseDto toDto(Category category);

    Category toModel(CategoryRequestDto categoryDto);

    List<CategoryResponseDto> toDtos(List<Category> list);
}
