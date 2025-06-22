package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "vw_table_autor")
@Getter
@Setter
public class VwTableAutor {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "\"Nome\"")
    private String nome;

    @Column(name = "\"Total de Livros\"")
    private Long totalDeLivros;
}
