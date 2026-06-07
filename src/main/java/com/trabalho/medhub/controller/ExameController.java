package com.trabalho.medhub.controller;

import com.trabalho.medhub.dto.exame.ExameRequestDTO;
import com.trabalho.medhub.dto.exame.ExameResponseDTO;
import com.trabalho.medhub.service.ExameService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exames")
public class ExameController {

    private final ExameService exameService;

    public ExameController(ExameService exameService) {
        this.exameService = exameService;
    }

    @PostMapping
    public ResponseEntity<ExameResponseDTO> cadastrar(@RequestBody @Valid ExameRequestDTO request) {
        ExameResponseDTO exame = exameService.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/exames/" + exame.id())).body(exame);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExameResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(exameService.buscarPorId(id));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ExameResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(exameService.listarPorPaciente(pacienteId));
    }
}
