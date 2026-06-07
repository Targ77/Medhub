package com.trabalho.medhub.repository;

import com.trabalho.medhub.entity.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long>, JpaSpecificationExecutor<Exame> {
}
