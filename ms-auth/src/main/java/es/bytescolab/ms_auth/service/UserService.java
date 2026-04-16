package es.bytescolab.ms_auth.service;

import es.bytescolab.ms_auth.dto.request.RegisterRequest;
import es.bytescolab.ms_auth.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest request);
}
