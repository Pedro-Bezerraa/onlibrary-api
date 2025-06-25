package com.onlibrary.onlibrary_api.dto.notificacao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlibrary.onlibrary_api.model.entities.Notificacao;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificacaoResponseDTO {

    private final UUID id;
    private final String titulo;
    private final String conteudo;

    @JsonProperty("data_emissao")
    private final LocalDate dataEmissao;

    @JsonProperty("marcado_lido")
    private final boolean marcadoLido;

    private final String tipo;

    @JsonProperty("biblioteca_id")
    private final UUID bibliotecaId;

    @JsonProperty("biblioteca_nome")
    private final String bibliotecaNome;

    public NotificacaoResponseDTO(Notificacao notificacao) {
        this.id = notificacao.getId();
        this.titulo = notificacao.getTitulo();
        this.conteudo = notificacao.getConteudo();
        this.dataEmissao = notificacao.getDataEmissao();
        this.marcadoLido = notificacao.getMarcadoLido();
        this.tipo = notificacao.getTipo().name();

        if (notificacao.getBiblioteca() != null) {
            this.bibliotecaId = notificacao.getBiblioteca().getId();
            this.bibliotecaNome = notificacao.getBiblioteca().getNome();
        } else {
            this.bibliotecaId = null;
            this.bibliotecaNome = null;
        }
    }
}