package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_categoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "categoria")
    private List<LivroCategoria> livros;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deletado = false;
}
