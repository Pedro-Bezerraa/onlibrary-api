package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("Nome")
    private String nome;

    @Column(name = "\"Telefone\"")
    @JsonProperty("Telefone")
    private String telefone;

    @Column(name = "\"Rua\"")
    @JsonProperty("Rua")
    private String rua;

    @Column(name = "\"Número\"")
    @JsonProperty("Número")
    private Integer numero;

    @Column(name = "\"Cep\"")
    @JsonProperty("CEP")
    private String cep;

    @Column(name = "\"Aplicação Multa\"")
    @JsonProperty("Aplicação multa")
    private Boolean aplicacaoMulta;

    @Column(name = "\"Reserva Online\"")
    @JsonProperty("Reserva online")
    private Boolean reservaOnline;

    @Column(name = "\"Aplicação Bloqueio\"")
    @JsonProperty("Aplicação bloqueio")
    private Boolean aplicacaoBloqueio;

    @Column(name = "\"Data de Emissão\"")
    @JsonProperty("Data de emissão")
    private LocalDate dataEmissao;
}
