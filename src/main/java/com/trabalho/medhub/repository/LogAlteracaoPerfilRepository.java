package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.LogAlteracaoPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAlteracaoPerfilRepository extends JpaRepository<LogAlteracaoPerfil, Long>, JpaSpecificationExecutor<LogAlteracaoPerfil> {
}
