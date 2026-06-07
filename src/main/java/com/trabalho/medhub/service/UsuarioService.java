package com.trabalho.medhub.service;

import com.trabalho.medhub.dto.medico.MedicoResponseDTO;
import com.trabalho.medhub.dto.usuario.AlterarPerfilRequestDTO;
import com.trabalho.medhub.dto.usuario.LogAlteracaoPerfilResponseDTO;
import com.trabalho.medhub.dto.usuario.UsuarioRequestDTO;
import com.trabalho.medhub.dto.usuario.UsuarioResponseDTO;
import com.trabalho.medhub.entity.Administrador;
import com.trabalho.medhub.entity.LogAlteracaoPerfil;
import com.trabalho.medhub.entity.Medico;
import com.trabalho.medhub.entity.Recepcionista;
import com.trabalho.medhub.entity.Usuario;
import com.trabalho.medhub.enums.PerfilUsuario;
import com.trabalho.medhub.exception.BusinessException;
import com.trabalho.medhub.exception.ResourceNotFoundException;
import com.trabalho.medhub.mapper.UsuarioMapper;
import com.trabalho.medhub.repository.AdministradorRepository;
import com.trabalho.medhub.repository.LogAlteracaoPerfilRepository;
import com.trabalho.medhub.repository.MedicoRepository;
import com.trabalho.medhub.repository.UsuarioRepository;
import com.trabalho.medhub.specification.LogAlteracaoPerfilSpecification;
import com.trabalho.medhub.specification.MedicoSpecification;
import com.trabalho.medhub.specification.UsuarioSpecification;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final AdministradorRepository administradorRepository;
    private final LogAlteracaoPerfilRepository logAlteracaoPerfilRepository;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            MedicoRepository medicoRepository,
            AdministradorRepository administradorRepository,
            LogAlteracaoPerfilRepository logAlteracaoPerfilRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.medicoRepository = medicoRepository;
        this.administradorRepository = administradorRepository;
        this.logAlteracaoPerfilRepository = logAlteracaoPerfilRepository;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO request) {
        validarDuplicidadeUsuario(request.cpf(), request.email());

        Usuario usuario = montarUsuario(request);
        Usuario salvo = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        return UsuarioMapper.toResponse(buscarUsuario(id));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar(PerfilUsuario perfil) {
        List<Usuario> usuarios = usuarioRepository.findAll(
                UsuarioSpecification.porPerfil(perfil),
                Sort.by(Sort.Direction.ASC, "nome")
        );
        return usuarios.stream().map(UsuarioMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<MedicoResponseDTO> listarMedicosAtivos(String especialidade) {
        List<Medico> medicos = medicoRepository.findAll(
                MedicoSpecification.ativosComEspecialidade(especialidade),
                Sort.by(Sort.Direction.ASC, "nome")
        );

        return medicos.stream().map(UsuarioMapper::toMedicoResponse).toList();
    }

    @Transactional
    public UsuarioResponseDTO alterarPerfil(Long usuarioId, AlterarPerfilRequestDTO request) {
        Usuario usuario = buscarUsuario(usuarioId);
        Administrador administrador = administradorRepository.findById(request.administradorId())
                .orElseThrow(() -> new ResourceNotFoundException("Administrador responsável pela alteração não encontrado."));

        if (usuario.getPerfil() == PerfilUsuario.ADMINISTRADOR
                && request.perfilNovo() != PerfilUsuario.ADMINISTRADOR
                && usuarioRepository.count(UsuarioSpecification.ativosPorPerfil(PerfilUsuario.ADMINISTRADOR)) <= 1) {
            throw new BusinessException("Não é permitido remover o último administrador ativo do sistema.");
        }

        PerfilUsuario perfilAnterior = usuario.getPerfil();
        usuario.alterarPerfil(request.perfilNovo());

        LogAlteracaoPerfil log = new LogAlteracaoPerfil();
        log.setPerfilAnterior(perfilAnterior);
        log.setPerfilNovo(request.perfilNovo());
        log.setUsuarioAlterado(usuario);
        log.setAdministrador(administrador);

        logAlteracaoPerfilRepository.save(log);
        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO ativar(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        usuario.ativar();
        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO desativar(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        usuario.desativar();
        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<LogAlteracaoPerfilResponseDTO> listarLogsPorUsuario(Long usuarioId) {
        return logAlteracaoPerfilRepository.findAll(
                        LogAlteracaoPerfilSpecification.porUsuarioAlterado(usuarioId),
                        Sort.by(Sort.Direction.DESC, "dataHora")
                )
                .stream()
                .map(UsuarioMapper::toLogResponse)
                .toList();
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    private void validarDuplicidadeUsuario(String cpf, String email) {
        if (usuarioRepository.existsByCpf(cpf)) {
            throw new BusinessException("CPF já cadastrado para outro usuário.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("E-mail já cadastrado para outro usuário.");
        }
    }

    private Usuario montarUsuario(UsuarioRequestDTO request) {
        Usuario usuario = switch (request.perfil()) {
            case MEDICO -> montarMedico(request);
            case RECEPCIONISTA -> new Recepcionista();
            case ADMINISTRADOR -> new Administrador();
        };

        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setEmail(request.email());
        usuario.setSenha(request.senha());
        usuario.setPerfil(request.perfil());
        usuario.setAtivo(true);
        return usuario;
    }

    private Medico montarMedico(UsuarioRequestDTO request) {
        if (request.crm() == null || request.crm().isBlank()) {
            throw new BusinessException("CRM é obrigatório para usuários com perfil médico.");
        }

        if (request.especialidade() == null || request.especialidade().isBlank()) {
            throw new BusinessException("Especialidade é obrigatória para usuários com perfil médico.");
        }

        if (medicoRepository.existsByCrm(request.crm())) {
            throw new BusinessException("CRM já cadastrado para outro médico.");
        }

        Medico medico = new Medico();
        medico.setCrm(request.crm());
        medico.setEspecialidade(request.especialidade());
        return medico;
    }
}
