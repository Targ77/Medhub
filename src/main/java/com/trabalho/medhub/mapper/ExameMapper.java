package com.trabalho.medhub.mapper;

import com.trabalho.medhub.dto.exame.ExameResponseDTO;
import com.trabalho.medhub.entity.ArquivoExame;
import com.trabalho.medhub.entity.Exame;

public final class ExameMapper {

    private ExameMapper() {
    }

    public static ExameResponseDTO toResponse(Exame exame) {
        ArquivoExame arquivo = exame.getArquivo();
        return new ExameResponseDTO(
                exame.getId(),
                exame.getPaciente().getId(),
                exame.getPaciente().getNomeCompleto(),
                exame.getMedicoSolicitante().getId(),
                exame.getMedicoSolicitante().getNome(),
                exame.getTipo(),
                exame.getDataRealizacao(),
                exame.getResultadoTexto(),
                exame.getStatus(),
                arquivo != null ? arquivo.getId() : null,
                arquivo != null ? arquivo.getNomeArquivo() : null,
                arquivo != null ? arquivo.getFormato() : null,
                arquivo != null ? arquivo.getCaminho() : null
        );
    }
}
