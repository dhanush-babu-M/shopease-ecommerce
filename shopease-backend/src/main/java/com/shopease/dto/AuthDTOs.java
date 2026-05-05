package com.shopease.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDTOs {

    public static class RegisterRequest {
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @Email @NotBlank
        private String email;
        @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        @Email @NotBlank
        private String email;
        @NotBlank
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthResponse {
        private String token;
        private String email;
        private String firstName;
        private String lastName;
        private String role;

        public AuthResponse(String token, String email, String firstName, String lastName, String role) {
            this.token = token;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
        }

        public String getToken() { return token; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getRole() { return role; }
    }
}
