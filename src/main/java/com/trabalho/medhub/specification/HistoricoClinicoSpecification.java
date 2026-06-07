package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.HistoricoClinico;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class HistoricoClinicoSpecification {

    private HistoricoClinicoSpecification() {
    }

    public static Specification<HistoricoClinico> comFiltros(
            Long pacienteId,
            Long medicoId,
            LocalDate inicio,
            LocalDate fim
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (pacienteId != null) {
                predicates.add(criteriaBuilder.equal(root.get("paciente").get("id"), pacienteId));
            }

            if (medicoId != null) {
                predicates.add(criteriaBuilder.equal(root.get("medico").get("id"), medicoId));
            }

            if (inicio != null && fim != null) {
                predicates.add(criteriaBuilder.between(root.get("data"), inicio, fim));
            } else if (inicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("data"), inicio));
            } else if (fim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("data"), fim));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<HistoricoClinico> porPaciente(Long pacienteId) {
        return comFiltros(pacienteId, null, null, null);
    }
}
