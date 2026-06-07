package com.trabalho.medhub.service;

import com.trabalho.medhub.dto.paciente.PacienteRequestDTO;
import com.trabalho.medhub.dto.paciente.PacienteResponseDTO;
import com.trabalho.medhub.entity.ChaveAcesso;
import com.trabalho.medhub.entity.Paciente;
import com.trabalho.medhub.exception.BusinessException;
import com.trabalho.medhub.exception.ResourceNotFoundException;
import com.trabalho.medhub.mapper.PacienteMapper;
import com.trabalho.medhub.repository.ChaveAcessoRepository;
import com.trabalho.medhub.repository.PacienteRepository;
import com.trabalho.medhub.specification.PacienteSpecification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ChaveAcessoRepository chaveAcessoRepository;

    public PacienteService(PacienteRepository pacienteRepository, ChaveAcessoRepository chaveAcessoRepository) {
        this.pacienteRepository = pacienteRepository;
        this.chaveAcessoRepository = chaveAcessoRepository;
    }

    @Transactional
    public PacienteResponseDTO cadastrar(PacienteRequestDTO request) {
        if (pacienteRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF já cadastrado para outro paciente.");
        }

        Paciente paciente = new Paciente();
        paciente.setNomeCompleto(request.nomeCompleto());
        paciente.setCpf(request.cpf());
        paciente.setDataNascimento(request.dataNascimento());
        paciente.setTelefone(request.telefone());
        paciente.setEmail(request.email());

        ChaveAcesso chave = gerarChaveUnica(paciente);
        paciente.setChaveAcesso(chave);

        Paciente salvo = pacienteRepository.save(paciente);
        return PacienteMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        return PacienteMapper.toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> buscarPorTermo(String termo) {
        List<Paciente> pacientes = pacienteRepository.findAll(
                PacienteSpecification.nomeOuCpfContem(termo),
                Sort.by(Sort.Direction.ASC, "nomeCompleto")
        );

        return pacientes.stream().map(PacienteMapper::toResponse).toList();
    }

    protected Paciente buscarEntidadePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente não encontrado."));
    }

    private ChaveAcesso gerarChaveUnica(Paciente paciente) {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString();
        } while (chaveAcessoRepository.existsByCodigo(codigo));

        ChaveAcesso chave = new ChaveAcesso();
        chave.setCodigo(codigo);
        chave.setDataCriacao(LocalDateTime.now());
        chave.setDataExpiracao(LocalDateTime.now().plusDays(30));
        chave.setAtiva(true);
        chave.setPaciente(paciente);
        return chave;
    }
}
