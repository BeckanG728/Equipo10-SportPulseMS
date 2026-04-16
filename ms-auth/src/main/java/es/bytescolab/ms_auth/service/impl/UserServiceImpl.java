package es.bytescolab.ms_auth.service.impl;

import es.bytescolab.ms_auth.dto.request.RegisterRequest;
import es.bytescolab.ms_auth.dto.response.RegisterResponse;
import es.bytescolab.ms_auth.entity.User;
import es.bytescolab.ms_auth.mapper.UserMappper;
import es.bytescolab.ms_auth.repository.UserRepository;
import es.bytescolab.ms_auth.service.UserService;
import es.bytescolab.ms_auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMappper userMapper;

    private final PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        User user = userMapper.toEntity(request);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }


        return null;
    }
}
