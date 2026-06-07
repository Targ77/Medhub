package com.trabalho.medhub.dto.historico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record HistoricoClinicoRequestDTO(
        @NotNull Long pacienteId,
        @NotNull Long medicoId,
        @NotNull LocalDate data,
        @NotBlank String queixaPrincipal,
        @NotBlank String diagnostico,
        String observacoes
) {
}
