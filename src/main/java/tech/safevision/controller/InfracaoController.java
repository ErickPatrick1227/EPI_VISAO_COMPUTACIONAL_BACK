package tech.safevision.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.safevision.controller.dto.DashboardStatsResponse;
import tech.safevision.controller.dto.InfracaoRequest;
import tech.safevision.controller.dto.InfracaoResponse;
import tech.safevision.entities.Infracao;
import tech.safevision.repository.InfracaoRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class InfracaoController {

    private final SimpMessagingTemplate messagingTemplate;
    private final InfracaoRepository infracaoRepository;


    public InfracaoController(
            InfracaoRepository infracaoRepository,
            SimpMessagingTemplate messagingTemplate) {

        this.infracaoRepository = infracaoRepository;
        this.messagingTemplate = messagingTemplate;
    }

    //public InfracaoController(InfracaoRepository infracaoRepository) {
       // this.infracaoRepository = infracaoRepository;
   // }

    @PostMapping("/internal/infracoes")
    public ResponseEntity<InfracaoResponse> receberInfracao(
            @RequestBody InfracaoRequest request) {

        var infracao = new Infracao();

        infracao.setPessoaId(request.pessoaId());
        infracao.setMensagem(request.mensagem());
        infracao.setEvidenciaPath(request.evidenciaPath());

        infracao.setCameraId(
                request.cameraId() != null
                        ? request.cameraId()
                        : "camera-1"
        );

        var saved = infracaoRepository.save(infracao);

        var response = InfracaoResponse.from(saved);

        messagingTemplate.convertAndSend(
                "/topic/alertas",
                response
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/infracoes")
    public ResponseEntity<List<InfracaoResponse>> listarInfracoes(
            @RequestParam(required = false) String cameraId,
            @RequestParam(required = false) Boolean visualizada,
            @RequestParam(required = false) String mensagem
    ) {

        List<Infracao> lista;

        // câmera + visualizada
        if (cameraId != null && visualizada != null) {
            lista = infracaoRepository
                    .findByCameraIdAndVisualizadaOrderByCriadoEmDesc(
                            cameraId,
                            visualizada
                    );
        }

        // câmera
        else if (cameraId != null) {
            lista = infracaoRepository
                    .findByCameraIdOrderByCriadoEmDesc(cameraId);
        }

        // visualizada
        else if (visualizada != null) {
            lista = infracaoRepository
                    .findByVisualizadaOrderByCriadoEmDesc(visualizada);
        }

        // mensagem
        else if (mensagem != null) {
            lista = infracaoRepository
                    .findByMensagemContainingIgnoreCaseOrderByCriadoEmDesc(
                            mensagem
                    );
        }

        // todas
        else {
            lista = infracaoRepository.findAllByOrderByCriadoEmDesc();
        }

        var response = lista.stream()
                .map(InfracaoResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }
    @GetMapping("/infracoes/filtro")
    public ResponseEntity<List<InfracaoResponse>> filtrarInfracoes(
            @RequestParam(required = false) String cameraId,
            @RequestParam(required = false) Boolean visualizada
    ) {

        List<Infracao> lista;

        if (cameraId != null && visualizada != null) {
            lista = infracaoRepository
                    .findByCameraIdAndVisualizadaOrderByCriadoEmDesc(
                            cameraId,
                            visualizada
                    );

        } else if (cameraId != null) {
            lista = infracaoRepository
                    .findByCameraIdOrderByCriadoEmDesc(cameraId);

        } else if (visualizada != null) {
            lista = infracaoRepository
                    .findByVisualizadaOrderByCriadoEmDesc(visualizada);

        } else {
            lista = infracaoRepository.findAllByOrderByCriadoEmDesc();
        }

        return ResponseEntity.ok(
                lista.stream()
                        .map(InfracaoResponse::from)
                        .toList()
        );
    }

    @GetMapping("/infracoes/pendentes")
    public ResponseEntity<List<InfracaoResponse>> listarPendentes() {
        var lista = infracaoRepository.findByVisualizadaFalseOrderByCriadoEmDesc()
                .stream()
                .map(InfracaoResponse::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PutMapping("/infracoes/{id}/visualizar")
    public ResponseEntity<Void> marcarVisualizada(@PathVariable Long id) {
        infracaoRepository.findById(id).ifPresent(i -> {
            i.setVisualizada(true);
            infracaoRepository.save(i);
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<DashboardStatsResponse> getStats() {
        var inicioDia = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        long hoje = infracaoRepository.countInfracoesHoje(inicioDia);
        long pendentes = infracaoRepository.countByVisualizadaFalse();
        long total = infracaoRepository.count();

        return ResponseEntity.ok(new DashboardStatsResponse(hoje, pendentes, total));
    }
}