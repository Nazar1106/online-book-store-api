package com.example.bookstoreapp.dto.orderdto;

import com.example.bookstoreapp.entity.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateDto {

    @NotNull
    private Status status;
}
