package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_livro")
@Getter
@Setter
public class VwLivro {
    @Column(name = "capa")
    private String capa;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "autores")
    private String autores;

    @Column(name = "categorias")
    private String categorias;

    @Column(name = "generos")
    private String generos;

    @Column(name = "editoras")
    private String editoras;

    @Column(name = "\"ISBN\"")
    private String isbn;

    @Column(name = "ano_lancamento")
    private Integer anoLancamento;

    @Id
    @Column(name = "id")
    private UUID id;
}
