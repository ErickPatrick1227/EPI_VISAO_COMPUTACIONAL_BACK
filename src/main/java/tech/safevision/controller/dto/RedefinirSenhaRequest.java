package tech.safevision.controller.dto;

public record RedefinirSenhaRequest(String email, String code, String novaSenha) {}
