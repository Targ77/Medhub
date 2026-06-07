package com.trabalho.medhub.dto.consulta;

import jakarta.validation.constraints.NotBlank;

public record CancelarConsultaRequestDTO(
        @NotBlank String motivo
) {
}
