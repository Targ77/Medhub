package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.HistoricoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoricoClinicoRepository extends JpaRepository<HistoricoClinico, Long>, JpaSpecificationExecutor<HistoricoClinico> {
}
