package com.trabalho.medhub.dto.medico;

public record MedicoResponseDTO(
        Long id,
        String nome,
        String crm,
        String especialidade,
        Boolean ativo
) {
}
