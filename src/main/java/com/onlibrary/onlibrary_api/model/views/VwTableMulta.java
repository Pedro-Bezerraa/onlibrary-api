package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_table_multa")
@Getter
@Setter
public class VwTableMulta {
    @Column(name = "Username")
    private String username;

    @Column(name = "Nome")
    private String nome;

    @Column(name = "Bibliotecario")
    private String bibliotecario;

    @Column(name = "Valor")
    private String valor;

    @Column(name = "Data de emissão")
    private String dataDeEmissao;

    @Column(name = "Data de vencimento")
    private String dataDeVencimento;

    @Column(name = "Situação")
    private String situacao;

    @Id
    private UUID id;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
