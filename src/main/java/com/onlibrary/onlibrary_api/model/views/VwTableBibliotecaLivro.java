package com.onlibrary.onlibrary_api.model.views;

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
    private String titulo;

    @Column(name = "\"ISBN\"")
    private String isbn;

    @Column(name = "\"Exemplares\"")
    private Long exemplares;

    @Column(name = "\"Autores\"")
    private String autores;

    @Column(name = "\"Categorias\"")
    private String categorias;

    @Column(name = "\"Generos\"")
    private String generos;

    @Column(name = "\"Editoras\"")
    private String editoras;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
