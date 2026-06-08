package com.trabalho.medhub.service;

import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.CONSULTA_ID;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.DATA_HORA_CONSULTA;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.MEDICO_ID;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.PACIENTE_ID;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.agendarConsultaRequestPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.cancelarConsultaRequestPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.consultaAgendadaPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.medicoAtivoPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.medicoInativoPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.pacientePadrao;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trabalho.medhub.dto.consulta.CancelarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.ConsultaResponseDTO;
import com.trabalho.medhub.entity.Consulta;
import com.trabalho.medhub.enums.StatusConsulta;
import com.trabalho.medhub.exception.BusinessException;
import com.trabalho.medhub.exception.ResourceNotFoundException;
import com.trabalho.medhub.repository.ConsultaRepository;
import com.trabalho.medhub.repository.MedicoRepository;
import com.trabalho.medhub.repository.PacienteRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private ConsultaService consultaService;

    @Test
    void agendarDeveSalvarConsultaQuandoNaoHouverConflito() {
        when(pacienteRepository.findById(PACIENTE_ID)).thenReturn(Optional.of(pacientePadrao()));
        when(medicoRepository.findById(MEDICO_ID)).thenReturn(Optional.of(medicoAtivoPadrao()));
        when(consultaRepository.count(ArgumentMatchers.<Specification<Consulta>>any())).thenReturn(0L);
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> {
            Consulta consulta = invocation.getArgument(0, Consulta.class);
            consulta.setId(CONSULTA_ID);
            return consulta;
        });

        ConsultaResponseDTO response = consultaService.agendar(agendarConsultaRequestPadrao());

        assertThat(response.id()).isEqualTo(CONSULTA_ID);
        assertThat(response.pacienteId()).isEqualTo(PACIENTE_ID);
        assertThat(response.pacienteNome()).isEqualTo("Maria Silva");
        assertThat(response.medicoId()).isEqualTo(MEDICO_ID);
        assertThat(response.medicoNome()).isEqualTo("Dra. Ana Souza");
        assertThat(response.dataHora()).isEqualTo(DATA_HORA_CONSULTA);
        assertThat(response.status()).isEqualTo(StatusConsulta.AGENDADA);
        assertThat(response.motivoCancelamento()).isNull();

        verify(pacienteRepository).findById(PACIENTE_ID);
        verify(medicoRepository).findById(MEDICO_ID);
        verify(consultaRepository).count(ArgumentMatchers.<Specification<Consulta>>any());
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void agendarDeveLancarErroQuandoPacienteNaoForEncontrado() {
        when(pacienteRepository.findById(PACIENTE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.agendar(agendarConsultaRequestPadrao()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Paciente não encontrado.");

        verify(pacienteRepository).findById(PACIENTE_ID);
        verify(medicoRepository, never()).findById(any());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void agendarDeveLancarErroQuandoMedicoEstiverInativo() {
        when(pacienteRepository.findById(PACIENTE_ID)).thenReturn(Optional.of(pacientePadrao()));
        when(medicoRepository.findById(MEDICO_ID)).thenReturn(Optional.of(medicoInativoPadrao()));

        assertThatThrownBy(() -> consultaService.agendar(agendarConsultaRequestPadrao()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Não é possível agendar consulta para médico inativo.");

        verify(consultaRepository, never()).count(ArgumentMatchers.<Specification<Consulta>>any());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void agendarDeveLancarErroQuandoExistirConflitoDeHorario() {
        when(pacienteRepository.findById(PACIENTE_ID)).thenReturn(Optional.of(pacientePadrao()));
        when(medicoRepository.findById(MEDICO_ID)).thenReturn(Optional.of(medicoAtivoPadrao()));
        when(consultaRepository.count(ArgumentMatchers.<Specification<Consulta>>any())).thenReturn(1L);

        assertThatThrownBy(() -> consultaService.agendar(agendarConsultaRequestPadrao()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Já existe consulta agendada para este médico nesse horário.");

        verify(consultaRepository).count(ArgumentMatchers.<Specification<Consulta>>any());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void cancelarDeveAlterarStatusERegistrarMotivo() {
        Consulta consulta = consultaAgendadaPadrao();
        CancelarConsultaRequestDTO request = cancelarConsultaRequestPadrao();

        when(consultaRepository.findById(CONSULTA_ID)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0, Consulta.class));

        ConsultaResponseDTO response = consultaService.cancelar(CONSULTA_ID, request);

        assertThat(response.id()).isEqualTo(CONSULTA_ID);
        assertThat(response.status()).isEqualTo(StatusConsulta.CANCELADA);
        assertThat(response.motivoCancelamento()).isEqualTo("Paciente solicitou remarcação.");
        assertThat(response.dataHoraCancelamento()).isNotNull();

        verify(consultaRepository).findById(CONSULTA_ID);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void cancelarDeveLancarErroQuandoConsultaJaEstiverCancelada() {
        Consulta consulta = consultaAgendadaPadrao();
        consulta.cancelar("Cancelamento anterior.");

        when(consultaRepository.findById(CONSULTA_ID)).thenReturn(Optional.of(consulta));

        assertThatThrownBy(() -> consultaService.cancelar(CONSULTA_ID, cancelarConsultaRequestPadrao()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("A consulta já está cancelada.");

        verify(consultaRepository, never()).save(any());
    }

    @Test
    void buscarPorIdDeveRetornarConsultaQuandoExistir() {
        when(consultaRepository.findById(CONSULTA_ID)).thenReturn(Optional.of(consultaAgendadaPadrao()));

        ConsultaResponseDTO response = consultaService.buscarPorId(CONSULTA_ID);

        assertThat(response.id()).isEqualTo(CONSULTA_ID);
        assertThat(response.status()).isEqualTo(StatusConsulta.AGENDADA);

        verify(consultaRepository).findById(CONSULTA_ID);
    }

    @Test
    void buscarPorIdDeveLancarErroQuandoConsultaNaoExistir() {
        when(consultaRepository.findById(CONSULTA_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.buscarPorId(CONSULTA_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Consulta não encontrada.");
    }

    @Test
    void listarAgendaDeveRetornarConsultasOrdenadasPorDataHora() {
        Consulta consulta = consultaAgendadaPadrao();
        LocalDate dataFiltro = DATA_HORA_CONSULTA.toLocalDate();

        when(consultaRepository.findAll(ArgumentMatchers.<Specification<Consulta>>any(), any(Sort.class)))
                .thenReturn(List.of(consulta));

        List<ConsultaResponseDTO> response = consultaService.listarAgenda(MEDICO_ID, null, StatusConsulta.AGENDADA, dataFiltro);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).id()).isEqualTo(CONSULTA_ID);
        assertThat(response.get(0).status()).isEqualTo(StatusConsulta.AGENDADA);

        verify(consultaRepository).findAll(ArgumentMatchers.<Specification<Consulta>>any(), eq(Sort.by(Sort.Direction.ASC, "dataHora")));
    }

    @Test
    void listarPorPacienteDeveRetornarConsultasDoPaciente() {
        when(consultaRepository.findAll(ArgumentMatchers.<Specification<Consulta>>any(), any(Sort.class)))
                .thenReturn(List.of(consultaAgendadaPadrao()));

        List<ConsultaResponseDTO> response = consultaService.listarPorPaciente(PACIENTE_ID);

        assertThat(response).hasSize(1);
        assertThat(response.get(0).pacienteId()).isEqualTo(PACIENTE_ID);

        verify(consultaRepository).findAll(ArgumentMatchers.<Specification<Consulta>>any(), eq(Sort.by(Sort.Direction.DESC, "dataHora")));
    }
}
