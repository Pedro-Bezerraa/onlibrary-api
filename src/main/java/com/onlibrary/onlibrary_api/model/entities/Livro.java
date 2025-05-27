package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(name = "ano_lancamento", nullable = false)
    private Integer anoLancamento;

    @OneToMany(mappedBy = "livro")
    private List<LivroAutor> autores;

    @OneToMany(mappedBy = "livro")
    private List<LivroCategoria> categorias;

    @OneToMany(mappedBy = "livro")
    private List<LivroEditora> editoras;

    @OneToMany(mappedBy = "livro")
    private List<LivroGenero> generos;

    @OneToMany(mappedBy = "livro")
    private List<BibliotecaLivro> bibliotecaLivros;
}
