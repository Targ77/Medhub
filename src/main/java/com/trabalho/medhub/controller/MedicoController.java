package com.trabalho.medhub.controller;

import com.trabalho.medhub.dto.medico.MedicoResponseDTO;
import com.trabalho.medhub.service.UsuarioService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final UsuarioService usuarioService;

    public MedicoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> listarAtivos(@RequestParam(required = false) String especialidade) {
        return ResponseEntity.ok(usuarioService.listarMedicosAtivos(especialidade));
    }
}
