package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_exemplar_emprestimo")
@Getter
@Setter
public class VwExemplarEmprestimo {
    @Column(name = "titulo")
    private String titulo;

    @Column(name = "numero_tombo")
    private String numeroTombo;

    @Id
    @Column(name = "id_emprestimo")
    private UUID idEmprestimo;

}
