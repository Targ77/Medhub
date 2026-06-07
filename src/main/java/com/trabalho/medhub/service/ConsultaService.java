package com.trabalho.medhub.service;

import com.trabalho.medhub.dto.consulta.AgendarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.CancelarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.ConsultaResponseDTO;
import com.trabalho.medhub.entity.Consulta;
import com.trabalho.medhub.entity.Medico;
import com.trabalho.medhub.entity.Paciente;
import com.trabalho.medhub.enums.StatusConsulta;
import com.trabalho.medhub.exception.BusinessException;
import com.trabalho.medhub.exception.ResourceNotFoundException;
import com.trabalho.medhub.mapper.ConsultaMapper;
import com.trabalho.medhub.repository.ConsultaRepository;
import com.trabalho.medhub.repository.MedicoRepository;
import com.trabalho.medhub.repository.PacienteRepository;
import com.trabalho.medhub.specification.ConsultaSpecification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(
            ConsultaRepository consultaRepository,
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository
    ) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public ConsultaResponseDTO agendar(AgendarConsultaRequestDTO request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));

        Medico medico = medicoRepository.findById(request.medicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Médico não encontrado."));

        if (!Boolean.TRUE.equals(medico.getAtivo())) {
            throw new BusinessException("Não é possível agendar consulta para médico inativo.");
        }

        boolean existeConflito = consultaRepository.count(
                ConsultaSpecification.conflitoDeHorario(medico.getId(), request.dataHora())
        ) > 0;

        if (existeConflito) {
            throw new BusinessException("Já existe consulta agendada para este médico nesse horário.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(request.dataHora());
        consulta.setStatus(StatusConsulta.AGENDADA);

        return ConsultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional
    public ConsultaResponseDTO cancelar(Long consultaId, CancelarConsultaRequestDTO request) {
        Consulta consulta = buscarEntidadePorId(consultaId);

        if (consulta.getStatus() == StatusConsulta.CANCELADA) {
            throw new BusinessException("A consulta já está cancelada.");
        }

        consulta.cancelar(request.motivo());
        return ConsultaMapper.toResponse(consultaRepository.save(consulta));
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscarPorId(Long id) {
        return ConsultaMapper.toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarAgenda(Long medicoId, Long pacienteId, StatusConsulta status, LocalDate data) {
        LocalDate dataFiltro = data != null ? data : LocalDate.now();
        LocalDateTime inicio = dataFiltro.atStartOfDay();
        LocalDateTime fim = dataFiltro.atTime(LocalTime.MAX);

        return consultaRepository.findAll(
                        ConsultaSpecification.comFiltros(medicoId, pacienteId, status, inicio, fim),
                        Sort.by(Sort.Direction.ASC, "dataHora")
                )
                .stream()
                .map(ConsultaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findAll(
                        ConsultaSpecification.porPaciente(pacienteId),
                        Sort.by(Sort.Direction.DESC, "dataHora")
                )
                .stream()
                .map(ConsultaMapper::toResponse)
                .toList();
    }

    private Consulta buscarEntidadePorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada."));
    }
}
