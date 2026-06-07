package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.CancelamentoConsulta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelamentoConsultaRepository extends JpaRepository<CancelamentoConsulta, Long>, JpaSpecificationExecutor<CancelamentoConsulta> {

    Optional<CancelamentoConsulta> findByConsultaId(Long consultaId);

    List<CancelamentoConsulta> findByDataHoraCancelamentoBetweenOrderByDataHoraCancelamentoDesc(
            LocalDateTime inicio,
            LocalDateTime fim
    );
}
