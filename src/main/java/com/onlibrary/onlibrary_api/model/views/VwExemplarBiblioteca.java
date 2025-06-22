package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Entity
@Immutable
@Table(name = "vw_exemplar_biblioteca")
@Getter
@Setter
public class VwExemplarBiblioteca {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "numero_tombo")
    private String numeroTombo;

    @Column(name = "estante")
    private String estante;

    @Column(name = "prateleira")
    private String prateleira;

    @Column(name = "setor")
    private String setor;
}
