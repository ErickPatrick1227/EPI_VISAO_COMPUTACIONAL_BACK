package tech.safevision.controller.dto;

/** Payload enviado pelo Python ao registrar uma infração confirmada */
public record InfracaoRequest(
        Integer pessoaId,
        String mensagem,
        String evidenciaPath,
        String cameraId
) {}
