package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_exemplar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_livro")
    private Livro livro;

    private String numeroTombo;
    private Boolean disponivel;
    private String estante;
    private String prateleira;
    private String setor;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @OneToMany(mappedBy = "exemplar")
    private List<EmprestimoExemplar> emprestimos;

    @OneToMany(mappedBy = "exemplar")
    private List<ReservaExemplar> reservas;
}
