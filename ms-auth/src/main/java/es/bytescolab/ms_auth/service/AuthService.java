package es.bytescolab.ms_auth.service;

import es.bytescolab.ms_auth.dto.request.LoginRequest;
import es.bytescolab.ms_auth.dto.request.RegisterRequest;
import es.bytescolab.ms_auth.dto.response.RegisterResponse;

import java.util.Map;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    Map<String, String> login(LoginRequest request);
}
