package tech.safevision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.safevision.entities.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
