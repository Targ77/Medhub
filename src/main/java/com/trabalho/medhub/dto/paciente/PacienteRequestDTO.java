package com.trabalho.medhub.dto.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PacienteRequestDTO(
        @NotBlank String nomeCompleto,
        @NotBlank String cpf,
        @NotNull LocalDate dataNascimento,
        @NotBlank String telefone,
        @Email String email
) {
}
