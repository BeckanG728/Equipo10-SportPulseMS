package es.bytescolab.ms_auth.service.impl;

import es.bytescolab.ms_auth.dto.request.LoginRequest;
import es.bytescolab.ms_auth.dto.request.RegisterRequest;
import es.bytescolab.ms_auth.dto.response.AuthResponse;
import es.bytescolab.ms_auth.dto.response.RegisterResponse;
import es.bytescolab.ms_auth.entity.User;
import es.bytescolab.ms_auth.exception.InvalidCredentialsException;
import es.bytescolab.ms_auth.exception.UserAlreadyExists;
import es.bytescolab.ms_auth.mapper.UserMappper;
import es.bytescolab.ms_auth.repository.UserRepository;
import es.bytescolab.ms_auth.service.AuthService;
import es.bytescolab.ms_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMappper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        User user = userMapper.toEntity(request);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExists("Ya existe un usuario con este usuario");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExists("Ya existe un usuario con este email");
        }

        User userWithEncodePassword = User.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(passwordEncoder.encode(request.password()))
                .role(user.getRole())
                .build();

        User saved = userRepository.save(userWithEncodePassword);

        return userMapper.toRegisterResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Email o contraseña incorrectos");
        }

        // Aqui se reutiliza el usuario autenticado (NO se hace otra query)
        User user = (User) authentication.getPrincipal();

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiration / 1000)
                .userId(user.getId().toString())
                .build();
    }
}
