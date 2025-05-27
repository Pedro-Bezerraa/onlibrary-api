package com.onlibrary.onlibrary_api.dto.usuario;

import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterRequestDTO {
    private String nome;
    private String sobrenome;
    private String username;
    private String email;
    private String senha;
    private String cpf;
    private ContaSituacao situacao;
}
