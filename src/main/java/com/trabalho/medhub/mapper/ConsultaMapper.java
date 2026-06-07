package com.trabalho.medhub.mapper;

import com.trabalho.medhub.dto.consulta.ConsultaResponseDTO;
import com.trabalho.medhub.entity.CancelamentoConsulta;
import com.trabalho.medhub.entity.Consulta;

public final class ConsultaMapper {

    private ConsultaMapper() {
    }

    public static ConsultaResponseDTO toResponse(Consulta consulta) {
        CancelamentoConsulta cancelamento = consulta.getCancelamento();
        return new ConsultaResponseDTO(
                consulta.getId(),
                consulta.getPaciente().getId(),
                consulta.getPaciente().getNomeCompleto(),
                consulta.getMedico().getId(),
                consulta.getMedico().getNome(),
                consulta.getDataHora(),
                consulta.getStatus(),
                cancelamento != null ? cancelamento.getMotivo() : null,
                cancelamento != null ? cancelamento.getDataHoraCancelamento() : null
        );
    }
}
