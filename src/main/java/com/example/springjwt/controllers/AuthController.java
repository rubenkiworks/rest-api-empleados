package com.example.springjwt.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjwt.models.ERole;
import com.example.springjwt.models.Role;
import com.example.springjwt.models.User;
import com.example.springjwt.payload.request.LoginRequest;
import com.example.springjwt.payload.request.SignupRequest;
import com.example.springjwt.payload.response.JwtResponse;
import com.example.springjwt.payload.response.MessageResponse;
import com.example.springjwt.repository.RoleRepository;
import com.example.springjwt.repository.UserRepository;
import com.example.springjwt.security.jwt.JwtUtils;
import com.example.springjwt.security.services.UserDetailsImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins="*", maxAge=3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new 
            UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Username is already taken"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
        }

        if (signUpRequest.getPassword().length() < 6)
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: Password no puede tener menos de 6 caracteres!"));

        if (signUpRequest.getPassword().length() > 40)
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: Password no puede tener mas de 40 caracteres!"));
        
        if (signUpRequest.getEmail() == null)
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: tiene que enviar un email!"));
        
        if (signUpRequest.getEmail().isBlank())
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: El email no puede ser una cadena vacía!"));

        if (signUpRequest.getUsername() == null)
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: tiene que enviar un username!"));
            
        if (signUpRequest.getUsername().isBlank())
            return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: El username no puede ser una cadena vacía!"));
        
        // Create new user's account
        // User user = new User(signUpRequest.getUsername(),
        //      signUpRequest.getEmail(),
        //      encoder.encode(signUpRequest.getPassword()));

        User user = User.builder()
            .username(signUpRequest.getUsername())
            .email(signUpRequest.getEmail())
            .password(encoder.encode(signUpRequest.getPassword()))
            .build();

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                 .orElseThrow(() -> new RuntimeException("Error: Role not found"));

            roles.add(userRole);
        } else {

            strRoles.forEach(role -> {
                switch (role) {
                    case "admin": Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);
                        break;
                    default: Role userRole = roleRepository.findByName(ERole.ROLE_USER) 
                        .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(userRole);
                        
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));

        
    }
}
