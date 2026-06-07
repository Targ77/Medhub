package com.trabalho.medhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cancelamentos_consulta")
public class CancelamentoConsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataHoraCancelamento;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String motivo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id", nullable = false, unique = true)
    private Consulta consulta;

    public CancelamentoConsulta(Consulta consulta, LocalDateTime dataHoraCancelamento, String motivo) {
        this.consulta = consulta;
        this.dataHoraCancelamento = dataHoraCancelamento;
        this.motivo = motivo;
    }
}
