package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.PerfilUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "medicos",
        uniqueConstraints = @UniqueConstraint(name = "uk_medico_crm", columnNames = "crm")
)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Medico extends Usuario {

    @NotBlank
    @Column(nullable = false, length = 30)
    private String crm;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String especialidade;

    @OneToMany(mappedBy = "medico")
    private Set<Consulta> consultas = new HashSet<>();

    @OneToMany(mappedBy = "medico")
    private Set<HistoricoClinico> historicosClinicos = new HashSet<>();

    @OneToMany(mappedBy = "medicoSolicitante")
    private Set<Exame> examesCadastrados = new HashSet<>();

    @OneToOne(mappedBy = "medico")
    private Agenda agenda;

    @PrePersist
    @PreUpdate
    private void atribuirPerfilPadrao() {
        setPerfil(PerfilUsuario.MEDICO);
    }
}
