package tech.safevision.controller.dto;

public record CriarUsuarioRequest(
        String username,
        String email,
        String password
) {}