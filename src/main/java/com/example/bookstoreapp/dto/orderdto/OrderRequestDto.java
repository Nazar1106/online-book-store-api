package com.example.bookstoreapp.dto.orderdto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {

    @NotBlank
    private String shippingAddress;

}
