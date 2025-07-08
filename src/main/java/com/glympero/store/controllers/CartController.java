package com.glympero.store.controllers;

import com.glympero.store.dtos.CartDto;
import com.glympero.store.entities.Cart;
import com.glympero.store.mappers.CartMapper;
import com.glympero.store.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;



    @PostMapping("")
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
       var cart = new Cart();
       cartRepository.save(cart);
       var cartDto = cartMapper.toDto(cart);

       var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cart.getId()).toUri();
       return ResponseEntity.created(uri).body(cartDto);
    }
}
