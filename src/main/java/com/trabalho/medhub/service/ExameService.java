package com.trabalho.medhub.service;

import com.trabalho.medhub.dto.exame.ArquivoExameRequestDTO;
import com.trabalho.medhub.dto.exame.ExameRequestDTO;
import com.trabalho.medhub.dto.exame.ExameResponseDTO;
import com.trabalho.medhub.entity.ArquivoExame;
import com.trabalho.medhub.entity.Exame;
import com.trabalho.medhub.entity.Medico;
import com.trabalho.medhub.entity.Paciente;
import com.trabalho.medhub.enums.StatusExame;
import com.trabalho.medhub.exception.BusinessException;
import com.trabalho.medhub.exception.ResourceNotFoundException;
import com.trabalho.medhub.mapper.ExameMapper;
import com.trabalho.medhub.repository.ExameRepository;
import com.trabalho.medhub.repository.MedicoRepository;
import com.trabalho.medhub.repository.PacienteRepository;
import com.trabalho.medhub.specification.ExameSpecification;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExameService {

    private final ExameRepository exameRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public ExameService(ExameRepository exameRepository, PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.exameRepository = exameRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public ExameResponseDTO cadastrar(ExameRequestDTO request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        Medico medico = medicoRepository.findById(request.medicoSolicitanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));

        if ((request.resultadoTexto() == null || request.resultadoTexto().isBlank()) && request.arquivo() == null) {
            throw new BusinessException("Informe o resultado em texto ou anexe um arquivo de exame.");
        }

        Exame exame = new Exame();
        exame.setPaciente(paciente);
        exame.setMedicoSolicitante(medico);
        exame.setTipo(request.tipo());
        exame.setDataRealizacao(request.dataRealizacao());
        exame.setResultadoTexto(request.resultadoTexto());
        exame.setStatus(StatusExame.DISPONIVEL);

        if (request.arquivo() != null) {
            exame.anexarArquivo(montarArquivo(request.arquivo()));
        }

        return ExameMapper.toResponse(exameRepository.save(exame));
    }

    @Transactional(readOnly = true)
    public ExameResponseDTO buscarPorId(Long id) {
        return ExameMapper.toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<ExameResponseDTO> listarPorPaciente(Long pacienteId) {
        return exameRepository.findAll(
                        ExameSpecification.porPaciente(pacienteId),
                        Sort.by(Sort.Direction.DESC, "dataRealizacao")
                )
                .stream()
                .map(ExameMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExameResponseDTO> listarDisponiveisPorPaciente(Long pacienteId) {
        return exameRepository.findAll(
                        ExameSpecification.porPacienteEStatus(
                                pacienteId,
                                List.of(StatusExame.CADASTRADO, StatusExame.DISPONIVEL)
                        ),
                        Sort.by(Sort.Direction.DESC, "dataRealizacao")
                )
                .stream()
                .map(ExameMapper::toResponse)
                .toList();
    }

    private Exame buscarEntidadePorId(Long id) {
        return exameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exame não encontrado."));
    }

    private ArquivoExame montarArquivo(ArquivoExameRequestDTO request) {
        ArquivoExame arquivo = new ArquivoExame();
        arquivo.setNomeArquivo(request.nomeArquivo());
        arquivo.setFormato(request.formato());
        arquivo.setCaminho(request.caminho());
        arquivo.setTamanhoBytes(request.tamanhoBytes());
        return arquivo;
    }
}
