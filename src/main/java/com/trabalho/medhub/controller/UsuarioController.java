package com.trabalho.medhub.controller;

import com.trabalho.medhub.dto.usuario.AlterarPerfilRequestDTO;
import com.trabalho.medhub.dto.usuario.LogAlteracaoPerfilResponseDTO;
import com.trabalho.medhub.dto.usuario.UsuarioRequestDTO;
import com.trabalho.medhub.dto.usuario.UsuarioResponseDTO;
import com.trabalho.medhub.enums.PerfilUsuario;
import com.trabalho.medhub.service.UsuarioService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO request) {
        UsuarioResponseDTO usuario = usuarioService.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.id())).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar(@RequestParam(required = false) PerfilUsuario perfil) {
        return ResponseEntity.ok(usuarioService.listar(perfil));
    }

    @PutMapping("/{id}/perfil")
    public ResponseEntity<UsuarioResponseDTO> alterarPerfil(
            @PathVariable Long id,
            @RequestBody @Valid AlterarPerfilRequestDTO request
    ) {
        return ResponseEntity.ok(usuarioService.alterarPerfil(id, request));
    }

    @PutMapping("/{id}/ativacao")
    public ResponseEntity<UsuarioResponseDTO> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.ativar(id));
    }

    @PutMapping("/{id}/desativacao")
    public ResponseEntity<UsuarioResponseDTO> desativar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.desativar(id));
    }

    @GetMapping("/{id}/logs-alteracao-perfil")
    public ResponseEntity<List<LogAlteracaoPerfilResponseDTO>> listarLogsPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.listarLogsPorUsuario(id));
    }
}
