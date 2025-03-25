package com.example.bookstoreapp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookstoreapp.BookUtil;
import com.example.bookstoreapp.dto.bookdto.BookDto;
import com.example.bookstoreapp.dto.bookdto.BookSearchParametersDto;
import com.example.bookstoreapp.dto.bookdto.CreateBookRequestDto;
import com.example.bookstoreapp.dto.bookdto.UpdateBookDto;
import com.example.bookstoreapp.entity.Book;
import com.example.bookstoreapp.exception.EntityNotFoundException;
import com.example.bookstoreapp.mapper.BookMapper;
import com.example.bookstoreapp.repository.book.BookRepository;
import com.example.bookstoreapp.repository.book.BookSpecificationBuilder;
import com.example.bookstoreapp.service.impl.BookServiceImpl;
import jakarta.validation.ValidationException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private Specification<Book> specification;
    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;
    private CreateBookRequestDto requestDto;
    private Pageable pageable;
    private Page<Book> bookPage;

    @BeforeEach
    public void setUp() {
        requestDto = BookUtil.createBookRequestDto();
        book = BookUtil.createBook(requestDto);
        bookDto = BookUtil.createBookDto(book);
        pageable = BookUtil.createPageable();
        bookPage = BookUtil.createBookPage(pageable);
    }

    @DisplayName("Should save a book and return the corresponding BookDto")
    @Test
    public void save_WithValidField_ShouldReturnBookDto() {
        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto saveBook = bookService.save(requestDto);

        assertThat(saveBook).isEqualTo(bookDto);
        assertThat(saveBook).isNotNull();
        verify(bookRepository, times(1)).save(book);
    }

    @DisplayName("Should throw NullPointerException when request DTO is null")
    @Test
    public void save_WithNullRequestDto_ShouldThrowException() {
        String exceptionMessage = "Book is null";
        CreateBookRequestDto requestDto = new CreateBookRequestDto();

        when(bookMapper.toEntity(requestDto)).thenThrow(new NullPointerException(exceptionMessage));
        Exception ex = assertThrows(NullPointerException.class,
                () -> bookService.save(requestDto));

        assertEquals(exceptionMessage, ex.getMessage());
    }

    @DisplayName("Should throw ValidationException when price is negative in the save method")
    @Test
    public void save_WithIncorrectPrice_ShouldThrowException() {
        String exceptionMessage = "Incorrect id";
        requestDto.setPrice(BigDecimal.valueOf(-5));

        when(bookMapper.toEntity(requestDto)).thenThrow(new ValidationException(exceptionMessage));
        Exception ex = assertThrows(ValidationException.class,
                () -> bookService.save(requestDto));

        assertEquals(exceptionMessage, ex.getMessage());
    }

    @DisplayName("Should return paginated list of BookDto")
    @Test
    void findAll_ShouldReturnBookDtoPage() {
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(bookDto);
    }

    @DisplayName("Should return BookDto when book exists")
    @Test
    void getBookById_WithExistingId_ShouldReturnBookDto() {
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(bookDto);
    }

    @DisplayName("Should throw EntityNotFoundException when book does not exist")
    @Test
    void getBookById_WithNonExistingId_ShouldThrowException() {
        String exceptionMs = "Can't find book by id ";
        Long id = 1L;

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(id));

        assertThat(exception.getMessage()).isEqualTo(exceptionMs + id);
    }

    @Test
    @DisplayName("Should return book list when matching books are found")
    void searchBooks_WithValidParams_ShouldReturnBookList() {
        BookSearchParametersDto searchParams = new BookSearchParametersDto(
                new String[]{"Java"},
                new String[]{"Author Name"},
                new BigDecimal[]{BigDecimal.valueOf(29.99)},
                new String[]{"1234567890"}
        );

        specification = mock(Specification.class);
        List<Book> bookList = List.of(book);
        List<BookDto> bookDtoList = List.of(bookDto);

        when(bookSpecificationBuilder.build(searchParams)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(bookList);
        when(bookMapper.toDtos(bookList)).thenReturn(bookDtoList);

        List<BookDto> result = bookService.searchBooks(searchParams);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should update book and return updated BookDto when book exists")
    void updateBook_WithValidId_ShouldReturnUpdatedBookDto() {
        Long bookId = 1L;
        UpdateBookDto updateBookDto = new UpdateBookDto();
        updateBookDto.setTitle("Updated Title");
        updateBookDto.setAuthor("Updated Author");

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthor("Updated Author");

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(bookId);
        updatedBookDto.setTitle("Updated Title");
        updatedBookDto.setAuthor("Updated Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookMapper).updateBookFromDto(updateBookDto, existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(updatedBookDto);

        BookDto result = bookService.updateBook(bookId, updateBookDto);

        assertThat(result).isEqualTo(updatedBookDto);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when book does not exist")
    void updateBook_WithInvalidId_ShouldThrowException() {
        Long bookId = 99L;
        UpdateBookDto updateBookDto = new UpdateBookDto();
        updateBookDto.setTitle("Updated Title");
        updateBookDto.setAuthor("Updated Author");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBook(bookId, updateBookDto));

        assertThat(exception.getMessage()).isEqualTo("Can't update book by id " + bookId);
    }

    @Test
    @DisplayName("Should delete book when book exists")
    void deleteById_WithValidId_ShouldDeleteBook() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(bookId);

        bookService.deleteById(bookId);

    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when book does not exist")
    void deleteById_WithInvalidId_ShouldThrowException() {
        Long bookId = 99L;
        String expectedMessage = "Can't delete book by id " + bookId;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteById(bookId));

        assertThat(exception.getMessage()).isEqualTo(expectedMessage);
    }
}
