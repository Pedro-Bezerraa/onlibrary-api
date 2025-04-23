package com.onlibrary.onlibrary_api.dto;

import com.onlibrary.onlibrary_api.model.ContaSituacao;
import com.onlibrary.onlibrary_api.model.UsuarioRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Builder
@Data
public class RegisterRequestDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String sobrenome;

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String senha;

    @CPF
    private String cpf;

    private ContaSituacao situacao;

    private UsuarioRole role;
}
