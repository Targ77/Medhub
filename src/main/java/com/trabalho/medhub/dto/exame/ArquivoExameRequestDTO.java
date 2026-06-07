package com.trabalho.medhub.dto.exame;

import com.trabalho.medhub.enums.FormatoArquivo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArquivoExameRequestDTO(
        @NotBlank String nomeArquivo,
        @NotNull FormatoArquivo formato,
        @NotBlank String caminho,
        @NotNull Long tamanhoBytes
) {
}
