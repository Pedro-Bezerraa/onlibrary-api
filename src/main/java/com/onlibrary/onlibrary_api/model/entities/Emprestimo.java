package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_emprestimo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @ManyToOne
    @JoinColumn(name = "fk_id_usuario_biblioteca")
    private UsuarioBiblioteca usuarioBiblioteca;

    @ManyToOne
    @JoinColumn(name = "fk_id_bibliotecario")
    private Usuario bibliotecario;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    private String situacao;

    @OneToMany(mappedBy = "emprestimo")
    private List<EmprestimoExemplar> exemplares;
}
