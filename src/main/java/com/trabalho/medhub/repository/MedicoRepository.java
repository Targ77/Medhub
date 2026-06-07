package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.Medico;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>, JpaSpecificationExecutor<Medico> {

    Optional<Medico> findByCpf(String cpf);

    Optional<Medico> findByEmail(String email);

    Optional<Medico> findByCrm(String crm);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    boolean existsByCrm(String crm);
}
