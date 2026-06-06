package tech.safevision.controller.dto;

import tech.safevision.entities.User;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record UsuarioResponse(
        UUID id,
        String username,
        String email,
        Set<String> roles
) {
    public static UsuarioResponse from(User u) {
        return new UsuarioResponse(
                u.getUserId(),
                u.getUsername(),
                u.getEmail(),
                u.getRoles().stream().map(r -> r.getName()).collect(Collectors.toSet())
        );
    }
}