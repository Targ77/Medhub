package com.trabalho.medhub.controller;

import com.trabalho.medhub.dto.paciente.PacienteRequestDTO;
import com.trabalho.medhub.dto.paciente.PacienteResponseDTO;
import com.trabalho.medhub.service.PacienteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid PacienteRequestDTO request) {
        PacienteResponseDTO paciente = pacienteService.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/pacientes/" + paciente.id())).body(paciente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> buscar(@RequestParam(required = false) String termo) {
        return ResponseEntity.ok(pacienteService.buscarPorTermo(termo));
    }
}
