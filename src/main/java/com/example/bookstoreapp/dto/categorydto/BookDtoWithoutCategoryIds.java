package com.example.bookstoreapp.dto.categorydto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDtoWithoutCategoryIds {

    private String title;

    private String author;

    private String isbn;

    private BigDecimal price;

    private String description;

    private String coverImage;
}
