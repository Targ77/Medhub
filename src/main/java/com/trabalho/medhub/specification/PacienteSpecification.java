package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.Paciente;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class PacienteSpecification {

    private PacienteSpecification() {
    }

    public static Specification<Paciente> nomeOuCpfContem(String termo) {
        return (root, query, criteriaBuilder) -> {
            if (termo == null || termo.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            String termoTexto = "%" + termo.trim().toLowerCase() + "%";
            String termoNumerico = termo.replaceAll("\\D", "");

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("nomeCompleto")),
                    termoTexto
            ));

            if (!termoNumerico.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        root.get("cpf"),
                        "%" + termoNumerico + "%"
                ));
            }

            return criteriaBuilder.or(predicates.toArray(Predicate[]::new));
        };
    }
}
