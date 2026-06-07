package com.trabalho.medhub.controller;

import com.trabalho.medhub.dto.historico.HistoricoClinicoRequestDTO;
import com.trabalho.medhub.dto.historico.HistoricoClinicoResponseDTO;
import com.trabalho.medhub.service.HistoricoClinicoService;
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
@RequestMapping("/api/historicos-clinicos")
public class HistoricoClinicoController {

    private final HistoricoClinicoService historicoClinicoService;

    public HistoricoClinicoController(HistoricoClinicoService historicoClinicoService) {
        this.historicoClinicoService = historicoClinicoService;
    }

    @PostMapping
    public ResponseEntity<HistoricoClinicoResponseDTO> registrar(@RequestBody @Valid HistoricoClinicoRequestDTO request) {
        HistoricoClinicoResponseDTO historico = historicoClinicoService.registrar(request);
        return ResponseEntity.created(URI.create("/api/historicos-clinicos/" + historico.id())).body(historico);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HistoricoClinicoResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(historicoClinicoService.listarPorPaciente(pacienteId));
    }
}
