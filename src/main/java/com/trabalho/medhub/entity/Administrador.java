package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.PerfilUsuario;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "administradores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Administrador extends Usuario {

    @OneToMany(mappedBy = "administrador")
    private Set<LogAlteracaoPerfil> logsAlteracaoPerfil = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void atribuirPerfilPadrao() {
        setPerfil(PerfilUsuario.ADMINISTRADOR);
    }
}
