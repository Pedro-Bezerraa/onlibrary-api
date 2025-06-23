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
@Table(name = "vw_table_biblioteca_livro")
@Getter
@Setter
public class VwTableBibliotecaLivro {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Título\"")
    @JsonProperty("Título")
    private String titulo;

    @Column(name = "\"ISBN\"")
    @JsonProperty("ISBN")
    private String isbn;

    @Column(name = "\"Exemplares\"")
    @JsonProperty("Exemplares")
    private Long exemplares;

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

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
