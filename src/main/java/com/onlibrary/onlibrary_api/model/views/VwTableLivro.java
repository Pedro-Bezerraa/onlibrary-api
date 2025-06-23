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
@Table(name = "vw_table_livro")
@Getter
@Setter
public class VwTableLivro {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Título\"")
    @JsonProperty("Título")
    private String titulo;

    @Column(name = "\"Ano de Lançamento\"")
    @JsonProperty("Ano de lançamento")
    private Integer anoLancamento;

    @Column(name = "\"ISBN\"")
    @JsonProperty("ISBN")
    private String isbn;

    @Column(name = "\"Descrição\"")
    @JsonProperty("Descrição")
    private String descricao;

    @Column(name = "\"Autores\"")
    @JsonProperty("Autores")
    private String autores;

    @Column(name = "\"Categorias\"")
    @JsonProperty("Categorias")
    private String categorias;

    @Column(name = "\"Generos\"")
    @JsonProperty("Gêneros")
    private String generos;

    @Column(name = "\"Editoras\"")
    @JsonProperty("Editoras")
    private String editoras;
}
