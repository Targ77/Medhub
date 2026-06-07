package com.trabalho.medhub.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String erro,
        List<String> mensagens
) {
}
