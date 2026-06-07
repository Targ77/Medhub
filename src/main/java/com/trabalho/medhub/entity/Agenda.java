package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.StatusConsulta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "agendas")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", nullable = false, unique = true)
    private Medico medico;

    @OneToMany(mappedBy = "agenda")
    private Set<Consulta> consultas = new HashSet<>();

    @Column(nullable = false)
    private LocalTime horaInicio = LocalTime.of(8, 0);

    @Column(nullable = false)
    private LocalTime horaFim = LocalTime.of(18, 0);

    @Column(nullable = false)
    private Integer intervaloMinutos = 30;

    public List<LocalDateTime> listarHorariosDisponiveis(Medico medico, LocalDate data) {
        if (!agendaPertenceAoMedico(medico) || data == null) {
            return List.of();
        }

        List<LocalDateTime> horarios = new ArrayList<>();
        LocalDateTime horario = LocalDateTime.of(data, horaInicio);
        LocalDateTime fim = LocalDateTime.of(data, horaFim);

        while (horario.isBefore(fim)) {
            if (!verificarConflito(medico, horario)) {
                horarios.add(horario);
            }
            horario = horario.plusMinutes(intervaloMinutos);
        }

        return horarios;
    }

    public boolean verificarConflito(Medico medico, LocalDateTime dataHora) {
        if (!agendaPertenceAoMedico(medico) || dataHora == null) {
            return false;
        }

        return consultas.stream()
                .filter(consulta -> consulta.getStatus() != StatusConsulta.CANCELADA)
                .anyMatch(consulta -> dataHora.equals(consulta.getDataHora()));
    }

    private boolean agendaPertenceAoMedico(Medico medicoInformado) {
        if (this.medico == null || medicoInformado == null) {
            return false;
        }

        if (this.medico.getId() != null && medicoInformado.getId() != null) {
            return Objects.equals(this.medico.getId(), medicoInformado.getId());
        }

        return this.medico == medicoInformado;
    }
}
