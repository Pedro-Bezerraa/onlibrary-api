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
@Table(name = "vw_table_biblioteca_genero")
@Getter
@Setter
public class VwTableBiliotecaGenero {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Genero\"")
    @JsonProperty("Gênero")
    private String genero;

    @Column(name = "\"Livro\"")
    @JsonProperty("Livro")
    private String livro;

    @Column(name = "\"ISBN\"")
    @JsonProperty("ISBN")
    private String isbn;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
