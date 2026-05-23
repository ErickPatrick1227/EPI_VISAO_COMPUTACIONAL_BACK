package tech.safevision.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_infracoes")
public class Infracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID da pessoa rastreada pelo YOLO (track_id) */
    @Column(name = "pessoa_id")
    private Integer pessoaId;

    /** Mensagem da infração ex: "SEM EPI" */
    private String mensagem;

    /** Caminho relativo da imagem de evidência salva pelo Python */
    @Column(name = "evidencia_path")
    private String evidenciaPath;

    /** Nome ou ID da câmera que gerou a detecção */
    @Column(name = "camera_id")
    private String cameraId;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    /** true = já visualizada por um operador no dashboard */
    private boolean visualizada = false;

    @PrePersist
    public void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() { return id; }
    public Integer getPessoaId() { return pessoaId; }
    public void setPessoaId(Integer pessoaId) { this.pessoaId = pessoaId; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getEvidenciaPath() { return evidenciaPath; }
    public void setEvidenciaPath(String evidenciaPath) { this.evidenciaPath = evidenciaPath; }
    public String getCameraId() { return cameraId; }
    public void setCameraId(String cameraId) { this.cameraId = cameraId; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public boolean isVisualizada() { return visualizada; }
    public void setVisualizada(boolean visualizada) { this.visualizada = visualizada; }
}
