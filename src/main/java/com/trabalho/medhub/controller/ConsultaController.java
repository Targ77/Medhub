package com.trabalho.medhub.controller;

import com.trabalho.medhub.dto.consulta.AgendarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.CancelarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.ConsultaResponseDTO;
import com.trabalho.medhub.enums.StatusConsulta;
import com.trabalho.medhub.service.ConsultaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> agendar(@RequestBody @Valid AgendarConsultaRequestDTO request) {
        ConsultaResponseDTO consulta = consultaService.agendar(request);
        return ResponseEntity.created(URI.create("/api/consultas/" + consulta.id())).body(consulta);
    }

    @PutMapping("/{id}/cancelamento")
    public ResponseEntity<ConsultaResponseDTO> cancelar(
            @PathVariable Long id,
            @RequestBody @Valid CancelarConsultaRequestDTO request
    ) {
        return ResponseEntity.ok(consultaService.cancelar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @GetMapping("/agenda")
    public ResponseEntity<List<ConsultaResponseDTO>> listarAgenda(
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) Long pacienteId,
            @RequestParam(required = false) StatusConsulta status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return ResponseEntity.ok(consultaService.listarAgenda(medicoId, pacienteId, status, data));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaResponseDTO>> listarPorPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(consultaService.listarPorPaciente(pacienteId));
    }
}
