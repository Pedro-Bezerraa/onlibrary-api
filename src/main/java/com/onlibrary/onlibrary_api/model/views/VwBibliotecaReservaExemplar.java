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
@Table(name = "vw_biblioteca_reserva_exemplar")
@Getter
@Setter
public class VwBibliotecaReservaExemplar {
    @Id
    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "reserva_online")
    private Boolean reservaOnline;

    @Column(name = "fk_id_livro")
    private UUID fkIdLivro;

    @Column(name = "nome")
    private String nome;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "cep")
    private String cep;

    @Column(name = "quantidade")
    private Long quantidade;
}
