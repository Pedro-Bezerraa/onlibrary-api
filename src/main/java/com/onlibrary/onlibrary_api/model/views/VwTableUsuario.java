package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "vw_table_usuario")
@Getter
@Setter
public class VwTableUsuario {
    @Id
    @Column(name = "id")
    @JsonProperty("Id")
    private UUID id;

    @Column(name = "\"Nome\"")
    @JsonProperty("Nome")
    private String nome;

    @Column(name = "\"Email\"")
    @JsonProperty("Email")
    private String email;

    @Column(name = "\"Username\"")
    @JsonProperty("Username")
    private String username;

    @Column(name = "\"Cpf\"")
    @JsonProperty("CPF")
    private String cpf;

    @Column(name = "\"Situação\"")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "\"Tipo\"")
    @JsonProperty("Tipo de usuário")
    private String tipo;

    @Column(name = "\"Data de Emissão\"")
    @JsonProperty("Data de emissão")
    private String dataEmissao;
}
