package com.trabalho.medhub.mapper;

import com.trabalho.medhub.dto.paciente.PacienteResponseDTO;
import com.trabalho.medhub.entity.ChaveAcesso;
import com.trabalho.medhub.entity.Paciente;

public final class PacienteMapper {

    private PacienteMapper() {
    }

    public static PacienteResponseDTO toResponse(Paciente paciente) {
        ChaveAcesso chave = paciente.getChaveAcesso();
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNomeCompleto(),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getTelefone(),
                paciente.getEmail(),
                chave != null ? chave.getCodigo() : null
        );
    }
}
