package com.onlibrary.onlibrary_api.dto.usuario;

import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponseDTO {
    private String nome;
    private String sobrenome;
    private String username;
    private String email;
    private String cpf;
    private ContaSituacao situacao;
}
