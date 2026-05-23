package tech.safevision.controller.dto;

import tech.safevision.entities.Infracao;

import java.time.LocalDateTime;

public record InfracaoResponse(
        Long id,
        Integer pessoaId,
        String mensagem,
        String evidenciaUrl,
        String cameraId,
        LocalDateTime criadoEm,
        boolean visualizada
) {
    public static InfracaoResponse from(Infracao i) {
        // Monta uma URL pública para a imagem servida pelo Spring
        String url = i.getEvidenciaPath() != null
                ? "/evidencias/" + java.nio.file.Paths.get(i.getEvidenciaPath()).getFileName()
                : null;
        return new InfracaoResponse(
                i.getId(),
                i.getPessoaId(),
                i.getMensagem(),
                url,
                i.getCameraId(),
                i.getCriadoEm(),
                i.isVisualizada()
        );
    }
}
