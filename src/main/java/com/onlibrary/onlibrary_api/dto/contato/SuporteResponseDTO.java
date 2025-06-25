package com.onlibrary.onlibrary_api.dto.contato;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.model.entities.Contato;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
public class SuporteResponseDTO {

    private final UUID id;
    private final String conteudo;

    @JsonProperty("data_emissao")
    private final OffsetDateTime dataEmissao;

    @JsonProperty("marcado_lido")
    private final boolean marcadoLido;

    private final boolean concluido;

    @JsonProperty("usuario_nome")
    private final String usuarioNome;

    @JsonProperty("usuario_email")
    private final String usuarioEmail;

    public SuporteResponseDTO(Contato contato) {
        this.id = contato.getId();
        this.conteudo = contato.getConteudo();
        this.dataEmissao = contato.getDataEmissao();
        this.marcadoLido = contato.getMarcadoLido();
        this.concluido = contato.getConcluido();
        this.usuarioNome = contato.getUsuario().getUsername();
        this.usuarioEmail = contato.getUsuario().getEmail();
    }
}