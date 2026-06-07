package com.trabalho.medhub.dto.exame;

import com.trabalho.medhub.enums.FormatoArquivo;
import com.trabalho.medhub.enums.StatusExame;
import java.time.LocalDate;

public record ExameResponseDTO(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long medicoSolicitanteId,
        String medicoNome,
        String tipo,
        LocalDate dataRealizacao,
        String resultadoTexto,
        StatusExame status,
        Long arquivoId,
        String nomeArquivo,
        FormatoArquivo formato,
        String caminhoArquivo
) {
}
