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
@Table(name = "vw_livro")
@Getter
@Setter
public class VwLivro {
    @Id
    @Column(name = "id")
    @JsonProperty("Id")
    private UUID id;

    @Column(name = "capa")
    @JsonProperty("Capa")
    private String capa;

    @Column(name = "titulo")
    @JsonProperty("Título")
    private String titulo;

    @Column(name = "descricao")
    @JsonProperty("Descrição")
    private String descricao;

    @Column(name = "autores")
    @JsonProperty("Autores")
    private String autores;

    @Column(name = "categorias")
    @JsonProperty("Categorias")
    private String categorias;

    @Column(name = "generos")
    @JsonProperty("Gêneros")
    private String generos;

    @Column(name = "editoras")
    @JsonProperty("Editoras")
    private String editoras;

    @Column(name = "\"ISBN\"")
    @JsonProperty("ISBN")
    private String isbn;

    @Column(name = "ano_lancamento")
    @JsonProperty("Ano lançamento")
    private Integer anoLancamento;

    @Column(name = "fk_id_biblioteca_livro")
    private UUID fkIdBibliotecaLivro;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
