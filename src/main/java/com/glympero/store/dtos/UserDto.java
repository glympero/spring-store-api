package com.glympero.store.dtos;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

//import java.time.LocalDateTime;


@AllArgsConstructor
@Getter

public class UserDto {
//    @JsonIgnore // This field will be ignored in JSON serialization
//    @JsonProperty("user_id") // This will change the name in JSON to "user_id")
    private Long id;
    private String name;
    private String email;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createdAt;
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    private String phoneNumber;
}
