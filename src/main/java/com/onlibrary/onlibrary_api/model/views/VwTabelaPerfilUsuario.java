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
@Table(name = "vw_table_perfil_usuario")
@Getter
@Setter
public class VwTabelaPerfilUsuario {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Nome\"")
    private String nome;

    @Column(name = "\"Valor da Multa\"")
    private String valorDaMulta;

    @Column(name = "\"Prazo de devolução\"")
    private String prazoDeDevolucao;

    @Column(name = "\"Prazo de Multa\"")
    private String prazoDeMulta;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
