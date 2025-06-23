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
@Table(name = "vw_table_emprestimo")
@Getter
@Setter
public class VwTableEmprestimo {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Username\"")
    @JsonProperty("Username")
    private String username;

    @Column(name = "\"Livros\"")
    @JsonProperty("Livros")
    private String livros;

    @Column(name = "\"Exemplares\"")
    @JsonProperty("Exemplares")
    private String exemplares;

    @Column(name = "\"Bibliotecario\"")
    @JsonProperty("Bibliotecário")
    private String bibliotecario;

    @Column(name = "\"Data de Emissão\"")
    @JsonProperty("Data de emissão")
    private String dataEmissao;

    @Column(name = "\"Data de Devolução\"")
    @JsonProperty("Data de devolução")
    private String dataDevolucao;

    @Column(name = "\"Situação\"")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;
}
