package com.trabalho.medhub.dto.historico;

import java.time.LocalDate;

public record HistoricoClinicoResponseDTO(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long medicoId,
        String medicoNome,
        LocalDate data,
        String queixaPrincipal,
        String diagnostico,
        String observacoes
) {
}
