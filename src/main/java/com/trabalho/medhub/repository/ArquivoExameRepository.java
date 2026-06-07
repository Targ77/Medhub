package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.ArquivoExame;
import com.trabalho.medhub.enums.FormatoArquivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoExameRepository extends JpaRepository<ArquivoExame, Long>, JpaSpecificationExecutor<ArquivoExame> {

    List<ArquivoExame> findByFormato(FormatoArquivo formato);

    List<ArquivoExame> findByNomeArquivoContainingIgnoreCase(String nomeArquivo);
}
