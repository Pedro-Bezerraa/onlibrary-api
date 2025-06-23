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
import java.util.UUID;

@Entity
@Immutable
@Table(name = "vw_table_reserva")
@Getter
@Setter
public class VwTableReserva {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Username\"")
    @JsonProperty("Username")
    private String username;

    @Column(name = "\"Livro\"")
    @JsonProperty("Livro")
    private String livro;

    @Column(name = "\"Exemplares\"")
    @JsonProperty("Exemplares")
    private String exemplares;

    @Column(name = "\"Data de Emissão\"")
    @JsonProperty("Data de emissão")
    private String dataEmissao;

    @Column(name = "\"Data de Retirada\"")
    @JsonProperty("Data de retirada")
    private String dataRetirada;

    @Column(name = "\"Situação\"")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "tipo")
    @JsonProperty("Tipo")
    private String tipo;

    @Column(name = "\"Quantidade Total\"")
    @JsonProperty("Quantidade total")
    private BigDecimal quantidadeTotal;

    @Column(name = "\"Quantidade Pendente\"")
    @JsonProperty("Quantidade pendente")
    private BigDecimal quantidadePendente;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;
}
