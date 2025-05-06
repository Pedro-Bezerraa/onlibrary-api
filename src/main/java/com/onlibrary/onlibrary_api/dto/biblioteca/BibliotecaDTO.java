package com.onlibrary.onlibrary_api.dto.biblioteca;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BibliotecaDTO {
    private Long id;
    private String telefone;
    private Boolean aplicacaoMulta;
    private Boolean reservaOnline;
    private EnderecoDTO endereco;
}
