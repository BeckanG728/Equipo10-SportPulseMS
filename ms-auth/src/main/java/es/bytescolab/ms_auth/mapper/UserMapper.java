package es.bytescolab.ms_auth.mapper;


import es.bytescolab.ms_auth.dto.request.RegisterRequest;
import es.bytescolab.ms_auth.dto.response.RegisterResponse;
import es.bytescolab.ms_auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);

    RegisterResponse toRegisterResponse(User entity);
}
