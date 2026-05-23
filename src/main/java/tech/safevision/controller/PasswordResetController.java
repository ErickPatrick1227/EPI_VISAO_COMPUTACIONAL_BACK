package tech.safevision.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.safevision.controller.dto.RedefinirSenhaRequest;
import tech.safevision.controller.dto.SolicitarResetRequest;
import tech.safevision.controller.dto.ValidarCodigoRequest;
import tech.safevision.service.PasswordResetService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService service;

    public PasswordResetController(PasswordResetService service) {
        this.service = service;
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<Map<String, String>> solicitarReset(@RequestBody SolicitarResetRequest req) {
        String code = service.gerarCodigo(req.email());

        if (code != null) {

            System.out.println("==============================================");
            System.out.println("  CÓDIGO DE RECUPERAÇÃO para " + req.email());
            System.out.println("  Código: " + code);
            System.out.println("  Expira em 30 minutos.");
            System.out.println("==============================================");
        }

        // Sempre retorna a mesma mensagem (segurança)
        return ResponseEntity.ok(Map.of("message", "Se o e-mail estiver cadastrado, você receberá o código em breve."));
    }

    @PostMapping("/validar-codigo")
    public ResponseEntity<Map<String, Object>> validarCodigo(@RequestBody ValidarCodigoRequest req) {
        boolean valido = service.validarCodigo(req.email(), req.code());

        if (!valido) {
            return ResponseEntity.badRequest()
                    .body(Map.of("valido", false, "message", "Código inválido ou expirado."));
        }

        return ResponseEntity.ok(Map.of("valido", true, "message", "Código válido."));
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Map<String, String>> redefinirSenha(@RequestBody RedefinirSenhaRequest req) {
        boolean ok = service.redefinirSenha(req.email(), req.code(), req.novaSenha());

        if (!ok) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Código inválido, expirado ou e-mail não encontrado."));
        }

        return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso!"));
    }
}
