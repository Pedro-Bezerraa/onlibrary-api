package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_emprestimo_exemplar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoExemplar {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_emprestimo")
    private Emprestimo emprestimo;

    @ManyToOne
    @JoinColumn(name = "fk_id_exemplar")
    private Exemplar exemplar;
}
