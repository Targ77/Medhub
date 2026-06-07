package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.ChaveAcesso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChaveAcessoRepository extends JpaRepository<ChaveAcesso, Long>, JpaSpecificationExecutor<ChaveAcesso> {

    Optional<ChaveAcesso> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
