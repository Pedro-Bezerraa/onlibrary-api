package com.onlibrary.onlibrary_api.dto.biblioteca;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBibliotecaDTO {
    private String nome;

    @Valid
    private EnderecoDTO endereco;
    private String telefone;
    private Boolean aplicacaoMulta;
    private Boolean reservaOnline;
}
