package com.trabalho.medhub.dto.portal;

import com.trabalho.medhub.dto.exame.ExameResponseDTO;
import java.util.List;

public record PortalExamesResponseDTO(
        Long pacienteId,
        String pacienteNome,
        List<ExameResponseDTO> exames
) {
}
