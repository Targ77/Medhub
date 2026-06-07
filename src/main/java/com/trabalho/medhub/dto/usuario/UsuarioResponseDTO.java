package com.trabalho.medhub.dto.usuario;

import com.trabalho.medhub.enums.PerfilUsuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        PerfilUsuario perfil,
        Boolean ativo,
        String crm,
        String especialidade
) {
}
