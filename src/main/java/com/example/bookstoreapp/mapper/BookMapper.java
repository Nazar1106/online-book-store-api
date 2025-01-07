package com.example.bookstoreapp.mapper;

import com.example.bookstoreapp.config.MapperConfig;
import com.example.bookstoreapp.dto.BookDto;
import com.example.bookstoreapp.dto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.UpdateBookDto;
import com.example.bookstoreapp.entity.Book;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book updateBookFromDto(UpdateBookDto updateBookDto);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    List<BookDto> toDtos(List<Book> books);
}
