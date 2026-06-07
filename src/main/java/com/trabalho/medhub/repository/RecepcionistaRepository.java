package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.Recepcionista;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Long>, JpaSpecificationExecutor<Recepcionista> {

    Optional<Recepcionista> findByCpf(String cpf);

    Optional<Recepcionista> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    List<Recepcionista> findByAtivoTrue();
}
