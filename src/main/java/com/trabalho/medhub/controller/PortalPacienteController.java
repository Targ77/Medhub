package com.trabalho.medhub.controller;

import com.trabalho.medhub.dto.portal.PortalExamesResponseDTO;
import com.trabalho.medhub.service.PortalPacienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portal-paciente")
public class PortalPacienteController {

    private final PortalPacienteService portalPacienteService;

    public PortalPacienteController(PortalPacienteService portalPacienteService) {
        this.portalPacienteService = portalPacienteService;
    }

    @GetMapping("/exames")
    public ResponseEntity<PortalExamesResponseDTO> consultarExamesPorChave(@RequestParam String chave) {
        return ResponseEntity.ok(portalPacienteService.consultarExamesPorChave(chave));
    }
}
