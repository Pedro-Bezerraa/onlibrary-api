package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_biblioteca_livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BibliotecaLivro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @ManyToOne
    @JoinColumn(name = "fk_id_livro")
    private Livro livro;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deletado = false;
}
