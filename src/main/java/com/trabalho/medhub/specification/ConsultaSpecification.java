package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.Consulta;
import com.trabalho.medhub.enums.StatusConsulta;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class ConsultaSpecification {

    private ConsultaSpecification() {
    }

    public static Specification<Consulta> comFiltros(
            Long medicoId,
            Long pacienteId,
            StatusConsulta status,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (medicoId != null) {
                predicates.add(criteriaBuilder.equal(root.get("medico").get("id"), medicoId));
            }

            if (pacienteId != null) {
                predicates.add(criteriaBuilder.equal(root.get("paciente").get("id"), pacienteId));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (inicio != null && fim != null) {
                predicates.add(criteriaBuilder.between(root.get("dataHora"), inicio, fim));
            } else if (inicio != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataHora"), inicio));
            } else if (fim != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHora"), fim));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Consulta> porPaciente(Long pacienteId) {
        return (root, query, criteriaBuilder) -> {
            if (pacienteId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("paciente").get("id"), pacienteId);
        };
    }

    public static Specification<Consulta> porMedico(Long medicoId) {
        return (root, query, criteriaBuilder) -> {
            if (medicoId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("medico").get("id"), medicoId);
        };
    }

    public static Specification<Consulta> statusEm(Collection<StatusConsulta> status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("status").in(status);
        };
    }

    public static Specification<Consulta> conflitoDeHorario(Long medicoId, LocalDateTime dataHora) {
        return (root, query, criteriaBuilder) -> {
            if (medicoId == null || dataHora == null) {
                return criteriaBuilder.disjunction();
            }

            return criteriaBuilder.and(
                    criteriaBuilder.equal(root.get("medico").get("id"), medicoId),
                    criteriaBuilder.equal(root.get("dataHora"), dataHora),
                    criteriaBuilder.notEqual(root.get("status"), StatusConsulta.CANCELADA)
            );
        };
    }
}
