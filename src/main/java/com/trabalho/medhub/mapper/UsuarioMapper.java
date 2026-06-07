package com.trabalho.medhub.mapper;

import com.trabalho.medhub.dto.medico.MedicoResponseDTO;
import com.trabalho.medhub.dto.usuario.LogAlteracaoPerfilResponseDTO;
import com.trabalho.medhub.dto.usuario.UsuarioResponseDTO;
import com.trabalho.medhub.entity.LogAlteracaoPerfil;
import com.trabalho.medhub.entity.Medico;
import com.trabalho.medhub.entity.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioResponseDTO toResponse(Usuario usuario) {
        String crm = null;
        String especialidade = null;

        if (usuario instanceof Medico medico) {
            crm = medico.getCrm();
            especialidade = medico.getEspecialidade();
        }

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getCpf(),
                usuario.getEmail(),
                usuario.getPerfil(),
                usuario.getAtivo(),
                crm,
                especialidade
        );
    }

    public static MedicoResponseDTO toMedicoResponse(Medico medico) {
        return new MedicoResponseDTO(
                medico.getId(),
                medico.getNome(),
                medico.getCrm(),
                medico.getEspecialidade(),
                medico.getAtivo()
        );
    }

    public static LogAlteracaoPerfilResponseDTO toLogResponse(LogAlteracaoPerfil log) {
        return new LogAlteracaoPerfilResponseDTO(
                log.getId(),
                log.getDataHora(),
                log.getUsuarioAlterado().getId(),
                log.getUsuarioAlterado().getNome(),
                log.getAdministrador().getId(),
                log.getAdministrador().getNome(),
                log.getPerfilAnterior(),
                log.getPerfilNovo()
        );
    }
}
