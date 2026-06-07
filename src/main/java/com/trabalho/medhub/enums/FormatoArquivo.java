package com.trabalho.medhub.enums;

public enum FormatoArquivo {
    PDF,
    IMAGEM,
    TEXTO;

    public boolean ehPermitido() {
        return this == PDF || this == IMAGEM || this == TEXTO;
    }
}
