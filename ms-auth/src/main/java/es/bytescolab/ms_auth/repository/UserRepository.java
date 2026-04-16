package es.bytescolab.ms_auth.repository;

import es.bytescolab.ms_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
