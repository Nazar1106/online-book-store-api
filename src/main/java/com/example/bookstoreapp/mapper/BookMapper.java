package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.bookdto.UpdateBookDto;
import com.example.bookstoreapp.dto.categorydto.BookDtoWithoutCategoryIds;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.entity.Category;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "categories",
            expression = "java(toCategoriesFromIds(createBookRequestDto.getCategoryIds()))")
    Book toEntity(CreateBookRequestDto createBookRequestDto);

    @Mapping(target = "categoryIds",
            expression = "java(toCategoryIds(book.getCategories()))")
    BookDto toDto(Book book);

    @Mapping(target = "categories",
            expression = "java(toCategoriesFromIds(updateBookDto.getCategoryIds()))")
    void updateBookFromDto(UpdateBookDto updateBookDto, @MappingTarget Book book);

    List<BookDto> toDtos(List<Book> books);

    List<BookDtoWithoutCategoryIds> toDtoWithoutCategories(List<Book> book);

    default Set<Category> toCategoriesFromIds(List<Long> categoryIds) {
        if (categoryIds == null) {
            return new HashSet<>();
        }
        return categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }

    default List<Long> toCategoryIds(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .toList();
    }
}
