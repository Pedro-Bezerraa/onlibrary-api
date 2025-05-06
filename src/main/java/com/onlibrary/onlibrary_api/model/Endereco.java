package com.onlibrary.onlibrary_api.model;

import com.onlibrary.onlibrary_api.dto.biblioteca.EnderecoDTO;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Endereco {
    private String rua;
    private String numero;
    private String cep;
}
