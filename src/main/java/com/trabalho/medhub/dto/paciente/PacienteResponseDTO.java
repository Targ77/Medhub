package com.trabalho.medhub.dto.paciente;

import java.time.LocalDate;

public record PacienteResponseDTO(
        Long id,
        String nomeCompleto,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String email,
        String chaveAcesso
) {
}
