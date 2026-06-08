package com.trabalho.medhub.testutil;

import com.trabalho.medhub.dto.consulta.AgendarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.CancelarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.ConsultaResponseDTO;
import com.trabalho.medhub.entity.Consulta;
import com.trabalho.medhub.entity.Medico;
import com.trabalho.medhub.entity.Paciente;
import com.trabalho.medhub.enums.PerfilUsuario;
import com.trabalho.medhub.enums.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ConsultaTestDataFactory {

    private ConsultaTestDataFactory() {
    }

    public static final Long PACIENTE_ID = 1L;
    public static final Long MEDICO_ID = 2L;
    public static final Long CONSULTA_ID = 10L;
    public static final LocalDateTime DATA_HORA_CONSULTA = LocalDateTime.of(2026, 6, 15, 10, 0);

    public static Paciente pacientePadrao() {
        Paciente paciente = new Paciente();
        paciente.setId(PACIENTE_ID);
        paciente.setNomeCompleto("Maria Silva");
        paciente.setCpf("12345678901");
        paciente.setDataNascimento(LocalDate.of(1995, 4, 12));
        paciente.setTelefone("31999999999");
        paciente.setEmail("maria.silva@email.com");
        return paciente;
    }

    public static Medico medicoAtivoPadrao() {
        Medico medico = new Medico();
        medico.setId(MEDICO_ID);
        medico.setNome("Dra. Ana Souza");
        medico.setCpf("98765432100");
        medico.setEmail("ana.souza@medhub.com");
        medico.setSenha("senha-teste");
        medico.setPerfil(PerfilUsuario.MEDICO);
        medico.setAtivo(true);
        medico.setCrm("CRM-MG-12345");
        medico.setEspecialidade("Clínica Geral");
        return medico;
    }

    public static Medico medicoInativoPadrao() {
        Medico medico = medicoAtivoPadrao();
        medico.setAtivo(false);
        return medico;
    }

    public static Consulta consultaAgendadaPadrao() {
        Consulta consulta = new Consulta();
        consulta.setId(CONSULTA_ID);
        consulta.setPaciente(pacientePadrao());
        consulta.setMedico(medicoAtivoPadrao());
        consulta.setDataHora(DATA_HORA_CONSULTA);
        consulta.setStatus(StatusConsulta.AGENDADA);
        return consulta;
    }

    public static AgendarConsultaRequestDTO agendarConsultaRequestPadrao() {
        return new AgendarConsultaRequestDTO(PACIENTE_ID, MEDICO_ID, DATA_HORA_CONSULTA);
    }

    public static CancelarConsultaRequestDTO cancelarConsultaRequestPadrao() {
        return new CancelarConsultaRequestDTO("Paciente solicitou remarcação.");
    }

    public static ConsultaResponseDTO consultaResponseAgendadaPadrao() {
        return new ConsultaResponseDTO(
                CONSULTA_ID,
                PACIENTE_ID,
                "Maria Silva",
                MEDICO_ID,
                "Dra. Ana Souza",
                DATA_HORA_CONSULTA,
                StatusConsulta.AGENDADA,
                null,
                null
        );
    }

    public static ConsultaResponseDTO consultaResponseCanceladaPadrao() {
        return new ConsultaResponseDTO(
                CONSULTA_ID,
                PACIENTE_ID,
                "Maria Silva",
                MEDICO_ID,
                "Dra. Ana Souza",
                DATA_HORA_CONSULTA,
                StatusConsulta.CANCELADA,
                "Paciente solicitou remarcação.",
                LocalDateTime.of(2026, 6, 10, 9, 30)
        );
    }
}
