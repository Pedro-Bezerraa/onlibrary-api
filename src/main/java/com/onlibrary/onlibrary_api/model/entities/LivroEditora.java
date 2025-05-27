package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_livro_editora")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroEditora {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_livro")
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "fk_id_editora")
    private Editora editora;
}
