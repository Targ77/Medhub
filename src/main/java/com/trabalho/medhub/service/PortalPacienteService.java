package com.trabalho.medhub.service;

import com.trabalho.medhub.dto.portal.PortalExamesResponseDTO;
import com.trabalho.medhub.entity.ChaveAcesso;
import com.trabalho.medhub.entity.Paciente;
import com.trabalho.medhub.enums.StatusExame;
import com.trabalho.medhub.exception.BusinessException;
import com.trabalho.medhub.mapper.ExameMapper;
import com.trabalho.medhub.repository.ChaveAcessoRepository;
import com.trabalho.medhub.repository.ExameRepository;
import com.trabalho.medhub.specification.ChaveAcessoSpecification;
import com.trabalho.medhub.specification.ExameSpecification;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortalPacienteService {

    private final ChaveAcessoRepository chaveAcessoRepository;
    private final ExameRepository exameRepository;

    public PortalPacienteService(ChaveAcessoRepository chaveAcessoRepository, ExameRepository exameRepository) {
        this.chaveAcessoRepository = chaveAcessoRepository;
        this.exameRepository = exameRepository;
    }

    @Transactional(readOnly = true)
    public PortalExamesResponseDTO consultarExamesPorChave(String codigo) {
        ChaveAcesso chave = chaveAcessoRepository.findOne(
                        ChaveAcessoSpecification.comFiltros(codigo, true, null, null)
                )
                .orElseThrow(() -> new BusinessException("Chave de acesso inválida ou inativa."));

        if (!chave.validar(codigo)) {
            throw new BusinessException("Chave de acesso expirada ou inválida.");
        }

        Paciente paciente = chave.getPaciente();

        return new PortalExamesResponseDTO(
                paciente.getId(),
                paciente.getNomeCompleto(),
                exameRepository.findAll(
                                ExameSpecification.porPacienteEStatus(
                                        paciente.getId(),
                                        List.of(StatusExame.CADASTRADO, StatusExame.DISPONIVEL)
                                ),
                                Sort.by(Sort.Direction.DESC, "dataRealizacao")
                        )
                        .stream()
                        .map(ExameMapper::toResponse)
                        .toList()
        );
    }
}
