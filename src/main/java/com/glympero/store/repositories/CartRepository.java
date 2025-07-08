package com.glympero.store.repositories;

import com.glympero.store.entities.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
}
