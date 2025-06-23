package com.onlibrary.onlibrary_api.model.entities;

import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import com.onlibrary.onlibrary_api.model.enums.TipoReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_id_bibliotecario", nullable = true)
    private Usuario bibliotecario;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "data_retirada")
    private LocalDate dataRetirada;

    @Enumerated(EnumType.STRING)
    private SituacaoReserva situacao;

    @Enumerated(EnumType.STRING)
    private TipoReserva tipo;

    @Column(name = "quantidade_total")
    private BigDecimal quantidadeTotal;

    @Column(name = "quantidade_pendente")
    private BigDecimal quantidadePendente;

    @OneToOne
    @JoinColumn(name = "fk_id_livro")
    private Livro livro;

    @OneToMany(mappedBy = "reserva")
    private List<ReservaExemplar> exemplares;

    @OneToMany(mappedBy = "reserva")
    private List<Emprestimo> emprestimos;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deletado = false;
}
