package com.onlibrary.onlibrary_api.model.views;

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
    private String titulo;

    @Column(name = "\"Ano de Lançamento\"")
    private Integer anoLancamento;

    @Column(name = "\"ISBN\"")
    private String isbn;

    @Column(name = "\"Descrição\"")
    private String descricao;

    @Column(name = "\"Autores\"")
    private String autores;

    @Column(name = "\"Categorias\"")
    private String categorias;

    @Column(name = "\"Generos\"")
    private String generos;

    @Column(name = "\"Editoras\"")
    private String editoras;
}
