package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.PerfilUsuario;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "recepcionistas")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Recepcionista extends Usuario {

    @PrePersist
    @PreUpdate
    private void atribuirPerfilPadrao() {
        setPerfil(PerfilUsuario.RECEPCIONISTA);
    }
}
