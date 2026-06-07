package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.Agenda;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long>, JpaSpecificationExecutor<Agenda> {

    Optional<Agenda> findByMedicoId(Long medicoId);

    boolean existsByMedicoId(Long medicoId);
}
