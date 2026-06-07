package com.trabalho.medhub.dto.usuario;

import com.trabalho.medhub.enums.PerfilUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDTO(
        @NotBlank String nome,
        @NotBlank String cpf,
        @Email @NotBlank String email,
        @NotBlank String senha,
        @NotNull PerfilUsuario perfil,
        String crm,
        String especialidade
) {
}
