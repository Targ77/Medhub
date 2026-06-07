package com.trabalho.medhub.service;

import com.trabalho.medhub.dto.historico.HistoricoClinicoRequestDTO;
import com.trabalho.medhub.dto.historico.HistoricoClinicoResponseDTO;
import com.trabalho.medhub.entity.HistoricoClinico;
import com.trabalho.medhub.entity.Medico;
import com.trabalho.medhub.entity.Paciente;
import com.trabalho.medhub.exception.ResourceNotFoundException;
import com.trabalho.medhub.mapper.HistoricoClinicoMapper;
import com.trabalho.medhub.repository.HistoricoClinicoRepository;
import com.trabalho.medhub.repository.MedicoRepository;
import com.trabalho.medhub.repository.PacienteRepository;
import com.trabalho.medhub.specification.HistoricoClinicoSpecification;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoricoClinicoService {

    private final HistoricoClinicoRepository historicoClinicoRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public HistoricoClinicoService(
            HistoricoClinicoRepository historicoClinicoRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository
    ) {
        this.historicoClinicoRepository = historicoClinicoRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public HistoricoClinicoResponseDTO registrar(HistoricoClinicoRequestDTO request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        Medico medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));

        HistoricoClinico historico = new HistoricoClinico();
        historico.setPaciente(paciente);
        historico.setMedico(medico);
        historico.setData(request.data());
        historico.setQueixaPrincipal(request.queixaPrincipal());
        historico.setDiagnostico(request.diagnostico());
        historico.setObservacoes(request.observacoes());

        return HistoricoClinicoMapper.toResponse(historicoClinicoRepository.save(historico));
    }

    @Transactional(readOnly = true)
    public List<HistoricoClinicoResponseDTO> listarPorPaciente(Long pacienteId) {
        return historicoClinicoRepository.findAll(
                        HistoricoClinicoSpecification.porPaciente(pacienteId),
                        Sort.by(Sort.Direction.DESC, "data")
                )
                .stream()
                .map(HistoricoClinicoMapper::toResponse)
                .toList();
    }
}
