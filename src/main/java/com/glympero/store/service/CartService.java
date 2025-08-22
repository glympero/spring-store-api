package com.glympero.store.service;

import com.glympero.store.dtos.CartDto;
import com.glympero.store.dtos.CartItemDto;
import com.glympero.store.entities.Cart;
import com.glympero.store.entities.Product;
import com.glympero.store.exception.BadRequestException;
import com.glympero.store.exception.ResourceNotFoundException;
import com.glympero.store.mappers.CartMapper;
import com.glympero.store.repositories.CartRepository;
import com.glympero.store.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;


    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart with id " + cartId + " not found");

        }
       return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = getCartOrThrow(cartId);
        var product = getProductOrThrow(productId);

        var cartItem = cart.addItem(product);
        cartRepository.save(cart);

        return cartMapper.toCartItemDto(cartItem);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, int quantity) {
        var cart = getCartOrThrow(cartId);

        var cartItem = cart.getItem(productId);

        if (cartItem == null) {
            throw new BadRequestException("Cart item with id " + productId + " does not exist");
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toCartItemDto(cartItem);
    }

    public void removeItemFromCart(UUID cartId, Long productId) {
        var cart = getCartOrThrow(cartId);

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = getCartOrThrow(cartId);
        cart.clear();
        cartRepository.save(cart);
    }

    private Cart getCartOrThrow(UUID cartId) {
        return cartRepository.getCartWithItems(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found: " + cartId));
    }

    private Product getProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException("Invalid product ID: " + productId));
    }

    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }
}
