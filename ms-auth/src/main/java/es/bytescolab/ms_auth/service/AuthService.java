package es.bytescolab.ms_auth.service;

import es.bytescolab.ms_auth.dto.request.LoginRequest;
import es.bytescolab.ms_auth.dto.request.RegisterRequest;
import es.bytescolab.ms_auth.dto.response.AuthResponse;
import es.bytescolab.ms_auth.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
