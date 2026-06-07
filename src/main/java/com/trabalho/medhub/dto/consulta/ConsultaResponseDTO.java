package com.trabalho.medhub.dto.consulta;

import com.trabalho.medhub.enums.StatusConsulta;
import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long medicoId,
        String medicoNome,
        LocalDateTime dataHora,
        StatusConsulta status,
        String motivoCancelamento,
        LocalDateTime dataHoraCancelamento
) {
}
