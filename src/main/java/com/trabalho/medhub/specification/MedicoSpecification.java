package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.Medico;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class MedicoSpecification {

    private MedicoSpecification() {
    }

    public static Specification<Medico> ativosComEspecialidade(String especialidade) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.isTrue(root.get("ativo")));

            if (especialidade != null && !especialidade.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("especialidade")),
                        "%" + especialidade.trim().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
