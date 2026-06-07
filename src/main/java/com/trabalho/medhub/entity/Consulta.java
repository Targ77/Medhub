package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.StatusConsulta;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusConsulta status = StatusConsulta.AGENDADA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id")
    private Agenda agenda;

    @OneToOne(mappedBy = "consulta", cascade = CascadeType.ALL, orphanRemoval = true)
    private CancelamentoConsulta cancelamento;

    public void confirmar() {
        this.status = StatusConsulta.CONFIRMADA;
    }

    public void iniciarAtendimento() {
        this.status = StatusConsulta.EM_ATENDIMENTO;
    }

    public void finalizar() {
        this.status = StatusConsulta.REALIZADA;
    }

    public void cancelar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("O motivo do cancelamento deve ser informado.");
        }

        this.status = StatusConsulta.CANCELADA;
        this.cancelamento = new CancelamentoConsulta(this, LocalDateTime.now(), motivo);
    }

    @PrePersist
    private void preencherStatusPadrao() {
        if (status == null) {
            status = StatusConsulta.AGENDADA;
        }
    }
}
