package tech.safevision.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import tech.safevision.controller.dto.AlterarSenhaRequest;
import tech.safevision.repository.UserRepository;

import java.util.UUID;

@RestController
@RequestMapping("/api/configuracoes")
public class ConfiguracaoController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ConfiguracaoController(UserRepository userRepository,
                                  BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<String> alterarSenha(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AlterarSenhaRequest req) {

        UUID userId = UUID.fromString(jwt.getSubject());
        var userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var user = userOpt.get();

        if (!passwordEncoder.matches(req.senhaAtual(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Senha atual incorreta.");
        }

        if (req.novaSenha().length() < 6) {
            return ResponseEntity.badRequest().body("A nova senha deve ter ao menos 6 caracteres.");
        }

        user.setPassword(passwordEncoder.encode(req.novaSenha()));
        userRepository.save(user);
        return ResponseEntity.ok("Senha alterada com sucesso.");
    }
}