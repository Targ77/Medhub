package com.trabalho.medhub.dto.usuario;

import com.trabalho.medhub.enums.PerfilUsuario;
import java.time.LocalDateTime;

public record LogAlteracaoPerfilResponseDTO(
        Long id,
        LocalDateTime dataHora,
        Long usuarioAlteradoId,
        String usuarioAlteradoNome,
        Long administradorId,
        String administradorNome,
        PerfilUsuario perfilAnterior,
        PerfilUsuario perfilNovo
) {
}
