package tech.safevision.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.safevision.entities.Infracao;
import java.time.LocalDateTime;
import java.util.List;

public interface InfracaoRepository extends JpaRepository<Infracao, Long> {

    List<Infracao> findAllByOrderByCriadoEmDesc();

    List<Infracao> findByVisualizadaFalseOrderByCriadoEmDesc();

    long countByCriadoEmBetween(LocalDateTime inicio, LocalDateTime fim);

    long countByVisualizadaFalse();

    @Query("SELECT COUNT(i) FROM Infracao i WHERE i.criadoEm >= :inicio")
    long countInfracoesHoje(LocalDateTime inicio);
}
