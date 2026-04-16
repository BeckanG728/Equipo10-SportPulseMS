package es.bytescolab.ms_auth.service.impl;

import es.bytescolab.ms_auth.dto.request.LoginRequest;
import es.bytescolab.ms_auth.dto.request.RegisterRequest;
import es.bytescolab.ms_auth.dto.response.RegisterResponse;
import es.bytescolab.ms_auth.entity.User;
import es.bytescolab.ms_auth.exception.InvalidCredentialsException;
import es.bytescolab.ms_auth.exception.UserAlreadyExists;
import es.bytescolab.ms_auth.mapper.UserMappper;
import es.bytescolab.ms_auth.repository.UserRepository;
import es.bytescolab.ms_auth.service.AuthService;
import es.bytescolab.ms_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMappper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        User user = userMapper.toEntity(request);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExists("Ya existe un usuario con este username");
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
    public Map<String, String> login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return Map.of("token", token);
    }
}
