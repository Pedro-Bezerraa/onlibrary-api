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
@Table(name = "vw_table_biblioteca_categoria")
@Getter
@Setter
public class VwTableBibliotecaCategoria {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Categoria\"")
    private String categoria;

    @Column(name = "\"Livro\"")
    private String livro;

    @Column(name = "\"ISBN\"")
    private String isbn;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
