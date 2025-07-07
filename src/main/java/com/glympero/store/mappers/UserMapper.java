package com.glympero.store.mappers;

import com.glympero.store.dtos.RegisterUserRequest;
import com.glympero.store.dtos.UpdateUserRequest;
import com.glympero.store.dtos.UserDto;
import com.glympero.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring") // to create a Spring bean
public interface UserMapper {
//    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())") // Automatically set createdAt to current time
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest request, @MappingTarget User user);

}
