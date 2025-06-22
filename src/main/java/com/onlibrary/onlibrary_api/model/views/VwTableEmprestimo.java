package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_table_emprestimo")
@Getter
@Setter
public class VwTableEmprestimo {

    @Column(name = "Username")
    private String username;

    @Column(name = "Livros")
    private String livros;

    @Column(name = "Exemplares")
    private String exemplares;

    @Column(name = "Bibliotecario")
    private String bibliotecario;

    @Column(name = "Data de Emissão")
    @JsonProperty("Data de Emissão")
    private String dataDeEmissao;

    @Column(name = "Data de Devolução")
    @JsonProperty("Data de Devolução")
    private String dataDeDevolucao;

    @Column(name = "Situação")
    private String situacao;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;

    @Id
    private UUID id;
}
