package com.trabalho.medhub.dto.exame;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ExameRequestDTO(
        @NotNull Long pacienteId,
        @NotNull Long medicoSolicitanteId,
        @NotBlank String tipo,
        @NotNull LocalDate dataRealizacao,
        String resultadoTexto,
        @Valid ArquivoExameRequestDTO arquivo
) {
}
