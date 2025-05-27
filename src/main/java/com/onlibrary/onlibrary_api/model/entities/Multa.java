package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_multa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Multa {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_emprestimo")
    private Emprestimo emprestimo;

    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_id_bibliotecario")
    private Usuario bibliotecario;

    private Integer valor;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private String situacao;
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;
}
