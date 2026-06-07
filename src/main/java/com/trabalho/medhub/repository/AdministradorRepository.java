package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.Administrador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long>, JpaSpecificationExecutor<Administrador> {

    Optional<Administrador> findByCpf(String cpf);

    Optional<Administrador> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    List<Administrador> findByAtivoTrue();

    long countByAtivoTrue();
}
