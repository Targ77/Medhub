package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.ChaveAcesso;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ChaveAcessoSpecification {

    private ChaveAcessoSpecification() {
    }

    public static Specification<ChaveAcesso> comFiltros(
            String codigo,
            Boolean ativa,
            Long pacienteId,
            LocalDateTime expiradaAntesDe
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (codigo != null && !codigo.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("codigo"), codigo));
            }

            if (ativa != null) {
                predicates.add(criteriaBuilder.equal(root.get("ativa"), ativa));
            }

            if (pacienteId != null) {
                predicates.add(criteriaBuilder.equal(root.get("paciente").get("id"), pacienteId));
            }

            if (expiradaAntesDe != null) {
                predicates.add(criteriaBuilder.lessThan(root.get("dataExpiracao"), expiradaAntesDe));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
