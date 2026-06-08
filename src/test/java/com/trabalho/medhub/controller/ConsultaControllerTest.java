package com.trabalho.medhub.controller;

import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.CONSULTA_ID;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.DATA_HORA_CONSULTA;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.MEDICO_ID;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.PACIENTE_ID;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.agendarConsultaRequestPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.cancelarConsultaRequestPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.consultaResponseAgendadaPadrao;
import static com.trabalho.medhub.testutil.ConsultaTestDataFactory.consultaResponseCanceladaPadrao;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.trabalho.medhub.dto.consulta.AgendarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.CancelarConsultaRequestDTO;
import com.trabalho.medhub.dto.consulta.ConsultaResponseDTO;
import com.trabalho.medhub.enums.StatusConsulta;
import com.trabalho.medhub.exception.GlobalExceptionHandler;
import com.trabalho.medhub.service.ConsultaService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class ConsultaControllerTest {

    private MockMvc mockMvc;
    private JsonMapper objectMapper;

    @Mock
    private ConsultaService consultaService;

    @InjectMocks
    private ConsultaController consultaController;

    @BeforeEach
    void setUp() {
        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(conversionService);

        mockMvc = MockMvcBuilders.standaloneSetup(consultaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .setValidator(validator)
                .setConversionService(conversionService)
                .build();
    }

    @Test
    void agendarDeveRetornar201QuandoRequestForValido() throws Exception {
        ConsultaResponseDTO response = consultaResponseAgendadaPadrao();
        when(consultaService.agendar(any(AgendarConsultaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(agendarConsultaRequestPadrao())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/consultas/" + CONSULTA_ID))
                .andExpect(jsonPath("$.id").value(CONSULTA_ID))
                .andExpect(jsonPath("$.pacienteId").value(PACIENTE_ID))
                .andExpect(jsonPath("$.pacienteNome").value("Maria Silva"))
                .andExpect(jsonPath("$.medicoId").value(MEDICO_ID))
                .andExpect(jsonPath("$.medicoNome").value("Dra. Ana Souza"))
                .andExpect(jsonPath("$.dataHora").value(DATA_HORA_CONSULTA.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.status").value("AGENDADA"));

        verify(consultaService).agendar(any(AgendarConsultaRequestDTO.class));
    }

    @Test
    void agendarNaoDeveChamarServiceQuandoRequestForInvalido() throws Exception {
        AgendarConsultaRequestDTO requestInvalido = new AgendarConsultaRequestDTO(null, MEDICO_ID, DATA_HORA_CONSULTA);

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        verify(consultaService, never()).agendar(any(AgendarConsultaRequestDTO.class));
    }

    @Test
    void cancelarDeveRetornar200QuandoMotivoForInformado() throws Exception {
        ConsultaResponseDTO response = consultaResponseCanceladaPadrao();
        when(consultaService.cancelar(eq(CONSULTA_ID), any(CancelarConsultaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/consultas/{id}/cancelamento", CONSULTA_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelarConsultaRequestPadrao())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CONSULTA_ID))
                .andExpect(jsonPath("$.status").value("CANCELADA"))
                .andExpect(jsonPath("$.motivoCancelamento").value("Paciente solicitou remarcação."));

        verify(consultaService).cancelar(eq(CONSULTA_ID), any(CancelarConsultaRequestDTO.class));
    }

    @Test
    void buscarPorIdDeveRetornarConsultaSimulada() throws Exception {
        ConsultaResponseDTO response = consultaResponseAgendadaPadrao();
        when(consultaService.buscarPorId(CONSULTA_ID)).thenReturn(response);

        mockMvc.perform(get("/api/consultas/{id}", CONSULTA_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CONSULTA_ID))
                .andExpect(jsonPath("$.status").value("AGENDADA"));

        verify(consultaService).buscarPorId(CONSULTA_ID);
    }

    @Test
    void listarAgendaDeveRetornarConsultasFiltradasPorMedicoStatusEData() throws Exception {
        ConsultaResponseDTO response = consultaResponseAgendadaPadrao();
        LocalDate dataFiltro = DATA_HORA_CONSULTA.toLocalDate();
        when(consultaService.listarAgenda(MEDICO_ID, null, StatusConsulta.AGENDADA, dataFiltro))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/consultas/agenda")
                        .param("medicoId", MEDICO_ID.toString())
                        .param("status", "AGENDADA")
                        .param("data", dataFiltro.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(CONSULTA_ID))
                .andExpect(jsonPath("$[0].medicoId").value(MEDICO_ID))
                .andExpect(jsonPath("$[0].status").value("AGENDADA"));

        verify(consultaService).listarAgenda(MEDICO_ID, null, StatusConsulta.AGENDADA, dataFiltro);
    }

    @Test
    void listarPorPacienteDeveRetornarConsultasDoPaciente() throws Exception {
        ConsultaResponseDTO response = consultaResponseAgendadaPadrao();
        when(consultaService.listarPorPaciente(PACIENTE_ID)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/consultas/paciente/{pacienteId}", PACIENTE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(CONSULTA_ID))
                .andExpect(jsonPath("$[0].pacienteId").value(PACIENTE_ID));

        verify(consultaService).listarPorPaciente(PACIENTE_ID);
    }
}
