package com.trabalho.medhub.entity;

import com.trabalho.medhub.interfaces.Validavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "chaves_acesso",
        uniqueConstraints = @UniqueConstraint(name = "uk_chave_acesso_codigo", columnNames = "codigo")
)
public class ChaveAcesso implements Validavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String codigo;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataExpiracao;

    @Column(nullable = false)
    private Boolean ativa = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    public boolean validar(String codigoInformado) {
        return codigo != null
                && codigo.equals(codigoInformado)
                && validar();
    }

    @Override
    public boolean validar() {
        return Boolean.TRUE.equals(ativa)
                && dataExpiracao != null
                && LocalDateTime.now().isBefore(dataExpiracao);
    }

    public void desativar() {
        this.ativa = false;
    }

    @PrePersist
    private void preencherDataCriacao() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
}
