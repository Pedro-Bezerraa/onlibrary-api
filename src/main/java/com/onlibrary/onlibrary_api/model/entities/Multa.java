package com.onlibrary.onlibrary_api.model.entities;

import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;
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
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_id_bibliotecario")
    private Usuario bibliotecario;

    @ManyToOne
    @JoinColumn(name = "fk_id_emprestimo")
    private Emprestimo emprestimo;

    private Integer valor;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private SituacaoMulta situacao;
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deletado = false;
}
