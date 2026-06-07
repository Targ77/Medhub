package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.LogAlteracaoPerfil;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class LogAlteracaoPerfilSpecification {

    private LogAlteracaoPerfilSpecification() {
    }

    public static Specification<LogAlteracaoPerfil> comFiltros(
            Long usuarioAlteradoId,
            Long administradorId,
            LocalDateTime inicio,
            LocalDateTime fim
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (usuarioAlteradoId != null) {
                predicates.add(criteriaBuilder.equal(root.get("usuarioAlterado").get("id"), usuarioAlteradoId));
            }

            if (administradorId != null) {
                predicates.add(criteriaBuilder.equal(root.get("administrador").get("id"), administradorId));
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

    public static Specification<LogAlteracaoPerfil> porUsuarioAlterado(Long usuarioAlteradoId) {
        return comFiltros(usuarioAlteradoId, null, null, null);
    }
}
