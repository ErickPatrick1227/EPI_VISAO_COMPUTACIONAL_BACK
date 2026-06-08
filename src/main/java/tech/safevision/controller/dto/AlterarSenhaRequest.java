package tech.safevision.controller.dto;

public record AlterarSenhaRequest(
        String senhaAtual,
        String novaSenha
) {}