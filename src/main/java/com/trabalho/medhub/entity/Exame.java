package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.StatusExame;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "exames")
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String tipo;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataRealizacao;

    @Lob
    private String resultadoTexto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusExame status = StatusExame.CADASTRADO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_solicitante_id", nullable = false)
    private Medico medicoSolicitante;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "arquivo_exame_id")
    private ArquivoExame arquivo;

    public void anexarArquivo(ArquivoExame arquivo) {
        if (arquivo == null || !arquivo.validar()) {
            throw new IllegalArgumentException("Arquivo de exame inválido.");
        }

        this.arquivo = arquivo;
        arquivo.setExame(this);
    }

    public String visualizarResultado() {
        return resultadoTexto;
    }

    public boolean possuiArquivo() {
        return arquivo != null;
    }

    @PrePersist
    private void preencherStatusPadrao() {
        if (status == null) {
            status = StatusExame.CADASTRADO;
        }
    }
}
