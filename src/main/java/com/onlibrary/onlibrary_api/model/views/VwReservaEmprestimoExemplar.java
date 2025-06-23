package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "vw_reserva_emprestimo_exemplar")
@Getter
@Setter
public class VwReservaEmprestimoExemplar {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "fk_id_livro")
    private UUID fkIdLivro;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;

    @Column(name = "fk_id_emprestimo")
    private UUID fkIdEmprestimo;

    @Column(name = "fk_id_exemplar")
    private UUID fkIdExemplar;

    @Column(name = "situacao")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "data_emissao")
    @JsonProperty("Data de emissão")
    private LocalDate dataEmissao;

    @Column(name = "quantidade_total")
    @JsonProperty("Quantidade Total")
    private BigDecimal quantidadeTotal;

    @Column(name = "quantidade_pendente")
    @JsonProperty("Quantidade pendente")
    private BigDecimal quantidadePendente;
}
