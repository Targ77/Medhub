package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.Exame;
import com.trabalho.medhub.enums.StatusExame;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ExameSpecification {

    private ExameSpecification() {
    }

    public static Specification<Exame> comFiltros(
            Long pacienteId,
            Long medicoSolicitanteId,
            String tipo,
            Collection<StatusExame> status,
            LocalDate inicio,
            LocalDate fim
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (pacienteId != null) {
                predicates.add(criteriaBuilder.equal(root.get("paciente").get("id"), pacienteId));
            }

            if (medicoSolicitanteId != null) {
                predicates.add(criteriaBuilder.equal(root.get("medicoSolicitante").get("id"), medicoSolicitanteId));
            }

            if (tipo != null && !tipo.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("tipo")),
                        "%" + tipo.trim().toLowerCase() + "%"
                ));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(root.get("status").in(status));
            }

            if (inicio != null && fim != null) {
                predicates.add(criteriaBuilder.between(root.get("dataRealizacao"), inicio, fim));
            } else if (inicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataRealizacao"), inicio));
            } else if (fim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataRealizacao"), fim));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Exame> porPaciente(Long pacienteId) {
        return comFiltros(pacienteId, null, null, null, null, null);
    }

    public static Specification<Exame> porPacienteEStatus(Long pacienteId, Collection<StatusExame> status) {
        return comFiltros(pacienteId, null, null, status, null, null);
    }
}
