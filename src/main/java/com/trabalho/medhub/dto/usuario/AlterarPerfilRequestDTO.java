package com.trabalho.medhub.dto.usuario;

import com.trabalho.medhub.enums.PerfilUsuario;
import jakarta.validation.constraints.NotNull;

public record AlterarPerfilRequestDTO(
        @NotNull PerfilUsuario perfilNovo,
        @NotNull Long administradorId
) {
}
