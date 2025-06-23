package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Entity
@Immutable
@Table(name = "vw_table_multa")
@Getter
@Setter
public class VwTableMulta {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Username\"")
    @JsonProperty("Username")
    private String username;

    @Column(name = "\"Nome\"")
    @JsonProperty("Nome")
    private String nome;

    @Column(name = "\"Bibliotecario\"")
    @JsonProperty("Biblioteca")
    private String bibliotecario;

    @Column(name = "\"Valor\"")
    @JsonProperty("Valor")
    private String valor;

    @Column(name = "\"Data de emissão\"")
    @JsonProperty("Data de emissão")
    private String dataEmissao;

    @Column(name = "\"Data de vencimento\"")
    @JsonProperty("Data de vencimento")
    private String dataVencimento;

    @Column(name = "\"Situação\"")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
