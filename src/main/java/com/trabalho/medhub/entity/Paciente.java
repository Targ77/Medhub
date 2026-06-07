package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.StatusExame;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "pacientes",
        uniqueConstraints = @UniqueConstraint(name = "uk_paciente_cpf", columnNames = "cpf")
)
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nomeCompleto;

    @NotBlank
    @Column(nullable = false, length = 14)
    private String cpf;

    @NotNull
    @Column(nullable = false)
    private LocalDate dataNascimento;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String telefone;

    @Email
    @Column(length = 150)
    private String email;

    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChaveAcesso chaveAcesso;

    @OneToMany(mappedBy = "paciente")
    private Set<Consulta> consultas = new HashSet<>();

    @OneToMany(mappedBy = "paciente")
    private Set<HistoricoClinico> historicosClinicos = new HashSet<>();

    @OneToMany(mappedBy = "paciente")
    private Set<Exame> exames = new HashSet<>();

    public List<Exame> visualizarExames(ChaveAcesso chave) {
        if (!chavePertenceAoPaciente(chave) || !chave.validar()) {
            return List.of();
        }

        return exames.stream()
                .filter(exame -> exame.getStatus() == StatusExame.CADASTRADO
                        || exame.getStatus() == StatusExame.DISPONIVEL)
                .toList();
    }

    private boolean chavePertenceAoPaciente(ChaveAcesso chave) {
        return chave != null
                && chave.getPaciente() != null
                && id != null
                && id.equals(chave.getPaciente().getId());
    }
}
