package com.aldayhealthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private String token;
    private UserDto user;

    // Constructor to easily map your data in the controller
    public JwtResponse(String token, Long id, String name, String email, String role) {
        this.token = token;
        this.user = new UserDto(id, name, email, role);
    }

    // Nested class to perfectly match React's data.user expectation
    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserDto {
        private Long id;
        private String name;
        private String email;
        private String role;
    }
}