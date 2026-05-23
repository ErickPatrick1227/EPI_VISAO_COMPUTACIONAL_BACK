package tech.safevision.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.safevision.entities.PasswordResetToken;
import tech.safevision.repository.PasswordResetTokenRepository;
import tech.safevision.repository.UserRepository;

import java.security.SecureRandom;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                UserRepository userRepository,
                                BCryptPasswordEncoder passwordEncoder) {
        this.tokenRepository = tokenRepository;
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String gerarCodigo(String email) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return null;
        }

        // Remove tokens antigos deste e-mail
        tokenRepository.deleteAllByEmail(email);

        // Gera código de 6 dígitos
        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));

        var token = new PasswordResetToken();
        token.setEmail(email);
        token.setCode(code);
        tokenRepository.save(token);

        return code;
    }

    public boolean validarCodigo(String email, String code) {
        var tokenOpt = tokenRepository.findByEmailAndCodeAndUsadoFalse(email, code);
        if (tokenOpt.isEmpty()) return false;
        return !tokenOpt.get().isExpirado();
    }

    public boolean redefinirSenha(String email, String code, String novaSenha) {
        var tokenOpt = tokenRepository.findByEmailAndCodeAndUsadoFalse(email, code);
        if (tokenOpt.isEmpty()) return false;

        var token = tokenOpt.get();
        if (token.isExpirado()) return false;

        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return false;

        var user = userOpt.get();
        user.setPassword(passwordEncoder.encode(novaSenha));
        userRepository.save(user);

        // Marca o token como usado
        token.setUsado(true);
        tokenRepository.save(token);

        return true;
    }
}