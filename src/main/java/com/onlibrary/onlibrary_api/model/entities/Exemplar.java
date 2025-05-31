package com.onlibrary.onlibrary_api.model.entities;

import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
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

    @Column(name = "numero_tombo")
    private String numeroTombo;
    private String estante;
    private String prateleira;
    private String setor;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @Enumerated(EnumType.STRING)
    private SituacaoExemplar situacao;

    @OneToMany(mappedBy = "exemplar")
    private List<EmprestimoExemplar> emprestimos;

    @OneToMany(mappedBy = "exemplar")
    private List<ReservaExemplar> reservas;
}
