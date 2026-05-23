package tech.safevision.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.safevision.entities.Role;
import tech.safevision.entities.User;
import tech.safevision.repository.RoleRepository;
import tech.safevision.repository.UserRepository;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        var adminRole = roleRepository.findByName(Role.Values.ADMIN.name())
                .orElseGet(() -> {
                    var r = new Role();
                    r.setName(Role.Values.ADMIN.name());
                    return roleRepository.save(r);
                });

        roleRepository.findByName(Role.Values.OPERADOR.name())
                .orElseGet(() -> {
                    var r = new Role();
                    r.setName(Role.Values.OPERADOR.name());
                    return roleRepository.save(r);
                });

        userRepository.findByUsername("admin").orElseGet(() -> {
            var admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@safevision.com"); // email padrão do admin
            admin.setRoles(Set.of(adminRole));
            return userRepository.save(admin);
        });
    }
}
