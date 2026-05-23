package tech.safevision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.safevision.entities.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
