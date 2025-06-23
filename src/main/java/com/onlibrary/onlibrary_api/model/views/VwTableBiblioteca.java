package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "vw_table_biblioteca")
@Getter
@Setter
public class VwTableBiblioteca {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Nome\"")
    private String nome;

    @Column(name = "\"Telefone\"")
    private String telefone;

    @Column(name = "\"Rua\"")
    private String rua;

    @Column(name = "\"Número\"")
    private Integer numero;

    @Column(name = "\"Cep\"")
    private String cep;

    @Column(name = "\"Aplicação Multa\"")
    private Boolean aplicacaoMulta;

    @Column(name = "\"Reserva Online\"")
    private Boolean reservaOnline;

    @Column(name = "\"Aplicação Bloqueio\"")
    private Boolean aplicacaoBloqueio;

    @Column(name = "\"Data de Emissão\"")
    private LocalDate dataEmissao;
}
