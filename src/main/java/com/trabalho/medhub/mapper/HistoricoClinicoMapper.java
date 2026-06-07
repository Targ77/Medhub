package com.trabalho.medhub.mapper;

import com.trabalho.medhub.dto.historico.HistoricoClinicoResponseDTO;
import com.trabalho.medhub.entity.HistoricoClinico;

public final class HistoricoClinicoMapper {

    private HistoricoClinicoMapper() {
    }

    public static HistoricoClinicoResponseDTO toResponse(HistoricoClinico historico) {
        return new HistoricoClinicoResponseDTO(
                historico.getId(),
                historico.getPaciente().getId(),
                historico.getPaciente().getNomeCompleto(),
                historico.getMedico().getId(),
                historico.getMedico().getNome(),
                historico.getData(),
                historico.getQueixaPrincipal(),
                historico.getDiagnostico(),
                historico.getObservacoes()
        );
    }
}
