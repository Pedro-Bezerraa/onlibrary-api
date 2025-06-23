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
@Table(name = "vw_table_genero")
@Getter
@Setter
public class VwTableGenero {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Nome\"")
    @JsonProperty("Nome")
    private String nome;

    @Column(name = "\"Total de Livros\"")
    @JsonProperty("Total de livros")
    private Long totalDeLivros;
}
