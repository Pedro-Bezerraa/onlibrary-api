package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "vw_table_exemplar")
@Getter
@Setter
public class VwTableExemplar {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Titulo\"")
    @JsonProperty("Título")
    private String titulo;

    @Column(name = "\"Número Tombo\"")
    @JsonProperty("Número tombo")
    private String numeroTombo;

    @Column(name = "\"Estante\"")
    @JsonProperty("Estante")
    private String estante;

    @Column(name = "\"Prateleira\"")
    @JsonProperty("Prateleira")
    private String prateleira;

    @Column(name = "\"Setor\"")
    @JsonProperty("Setor")
    private String setor;

    @Column(name = "\"Situação\"")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
