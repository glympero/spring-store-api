package com.glympero.store.mappers;

import com.glympero.store.dtos.CartDto;
import com.glympero.store.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
