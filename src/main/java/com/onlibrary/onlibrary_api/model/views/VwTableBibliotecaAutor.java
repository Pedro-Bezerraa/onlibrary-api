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
@Table(name = "vw_table_biblioteca_autor")
@Getter
@Setter
public class VwTableBibliotecaAutor {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Autor\"")
    @JsonProperty("Autor")
    private String autor;

    @Column(name = "\"Livro\"")
    @JsonProperty("Livro")
    private String livro;

    @Column(name = "\"ISBN\"")
    @JsonProperty("ISBN")
    private String isbn;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
