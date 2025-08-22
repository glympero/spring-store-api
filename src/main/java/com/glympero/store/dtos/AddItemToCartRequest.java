package com.glympero.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotBlank(message = "Product id is required")
    private String productId;
}
