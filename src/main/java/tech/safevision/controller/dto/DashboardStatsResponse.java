package tech.safevision.controller.dto;

public record DashboardStatsResponse(
        long infracoesHoje,
        long infracoesPendentes,
        long totalInfracoes
) {}
