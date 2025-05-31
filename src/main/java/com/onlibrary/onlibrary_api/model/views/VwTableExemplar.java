package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_table_exemplar")
@Getter
@Setter
public class VwTableExemplar {
    @Column(name = "Titulo")
    private String titulo;

    @Column(name = "Número Tombo")
    private String numeroTombo;

    @Column(name = "Estante")
    private String estante;

    @Column(name = "Prateleira")
    private String prateleira;

    @Column(name = "Setor")
    private String setor;

    @Id
    private UUID id;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
