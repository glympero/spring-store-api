package com.glympero.store.controllers;

import com.glympero.store.dtos.AddItemToCartRequest;
import com.glympero.store.dtos.CartDto;
import com.glympero.store.dtos.CartItemDto;
import com.glympero.store.dtos.UpdateItemToCartRequest;
import com.glympero.store.exception.BadRequestException;
import com.glympero.store.exception.ResourceNotFoundException;
import com.glympero.store.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name="Carts", description = "Endpoints for managing shopping cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCartById(@PathVariable UUID cartId) {
//        var cart = cartRepository.findById(cartId).orElse(null);
        var cartDto = cartService.getCart(cartId);
        return ResponseEntity.ok(cartDto);
    }


    @PostMapping("")
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
       var cartDto = cartService.createCart();

       var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();
       return ResponseEntity.created(uri).body(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<CartItemDto> updateCartItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateItemToCartRequest request
    ) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, request.getQuantity());
        return ResponseEntity.ok(cartItemDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add a product to the cart")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "ID of the cart to which the product will be added", required = true)
            @PathVariable UUID cartId,
            @Valid @RequestBody AddItemToCartRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var cartItemDto = cartService.addToCart(cartId, Long.valueOf(request.getProductId()));
        var uri = uriBuilder.path("/carts/{cartId}/items/{itemId}")
                .buildAndExpand(cartId, cartId).toUri();
        return ResponseEntity.created(uri).body(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<Void> removeItemFromCart(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ) {
        cartService.removeItemFromCart(cartId, productId);

        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") UUID cartId)
    {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleCartNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Map<String, String>> handleProductNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", ex.getMessage()));
    }
}
