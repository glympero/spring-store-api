package com.glympero.store.controllers;

import com.glympero.store.config.JwtConfig;
import com.glympero.store.dtos.AuthenticateUserRequest;
//import com.glympero.store.repositories.UserRepository;
import com.glympero.store.dtos.JwtResponse;
import com.glympero.store.dtos.UserDto;
import com.glympero.store.mappers.UserMapper;
import com.glympero.store.repositories.UserRepository;
import com.glympero.store.service.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name="Auth", description = "Endpoints for managing authentication")
public class AuthController {
    private final UserMapper userMapper;
    private UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;



    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody AuthenticateUserRequest request,
            HttpServletResponse response

    ) {
        //we remove this since we implement Spring Security AuthenticationManager
//        var user =  userRepository.findByEmail(request.getEmail()).orElse(null);
//        if(user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true); // Prevents JavaScript access to the cookie
        cookie.setPath("/auth/refresh"); // Cookie is sent only to this endpoint
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); // 7 days in seconds
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //because in the filter we set the principal as email - id
        var userId = (Long) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @CookieValue(value="refreshToken") String refreshToken
    ) {
        var jtw = jwtService.parse(refreshToken);
        if (jtw == null || jtw.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = jtw.getUserId();
        var user = userRepository.findById(userId).orElseThrow();


        var newAccessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new JwtResponse(newAccessToken.toString()));

    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
