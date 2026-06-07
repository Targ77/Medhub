package com.trabalho.medhub.entity;

import com.trabalho.medhub.enums.FormatoArquivo;
import com.trabalho.medhub.interfaces.Validavel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "arquivos_exame")
public class ArquivoExame implements Validavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String nomeArquivo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FormatoArquivo formato;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String caminho;

    @NotNull
    @Column(nullable = false)
    private Long tamanhoBytes;

    @OneToOne(mappedBy = "arquivo")
    private Exame exame;

    @Override
    public boolean validar() {
        return formato != null
                && formato.ehPermitido()
                && caminho != null
                && !caminho.isBlank()
                && tamanhoBytes != null
                && tamanhoBytes >= 0;
    }
}
