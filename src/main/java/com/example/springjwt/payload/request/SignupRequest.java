package com.example.springjwt.payload.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SignupRequest {

    //@NotBlank
    //@Size(min=3, max=20)
    private String username;

    //@NotBlank
    //@Size(max=50)
    //@Email
    private String email;

    private Set<String> role;

    //@NotBlank
    //@Size(min=6, max=40, message="La contraseña debe tener entre 6 y 40 caracteres")
    private String password;
 }
