package com.trabalho.medhub.dto.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AgendarConsultaRequestDTO(
        @NotNull Long pacienteId,
        @NotNull Long medicoId,
        @Future @NotNull LocalDateTime dataHora
) {
}
