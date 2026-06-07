package com.trabalho.medhub.specification;

import com.trabalho.medhub.entity.Usuario;
import com.trabalho.medhub.enums.PerfilUsuario;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public final class UsuarioSpecification {

    private UsuarioSpecification() {
    }

    public static Specification<Usuario> comFiltros(PerfilUsuario perfil, Boolean ativo, String termo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (perfil != null) {
                predicates.add(criteriaBuilder.equal(root.get("perfil"), perfil));
            }

            if (ativo != null) {
                predicates.add(criteriaBuilder.equal(root.get("ativo"), ativo));
            }

            if (termo != null && !termo.isBlank()) {
                String termoTexto = "%" + termo.trim().toLowerCase() + "%";
                String termoNumerico = termo.replaceAll("\\D", "");

                List<Predicate> buscas = new ArrayList<>();
                buscas.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), termoTexto));
                buscas.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), termoTexto));

                if (!termoNumerico.isBlank()) {
                    buscas.add(criteriaBuilder.like(root.get("cpf"), "%" + termoNumerico + "%"));
                }

                predicates.add(criteriaBuilder.or(buscas.toArray(Predicate[]::new)));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static Specification<Usuario> porPerfil(PerfilUsuario perfil) {
        return comFiltros(perfil, null, null);
    }

    public static Specification<Usuario> ativosPorPerfil(PerfilUsuario perfil) {
        return comFiltros(perfil, true, null);
    }
}
