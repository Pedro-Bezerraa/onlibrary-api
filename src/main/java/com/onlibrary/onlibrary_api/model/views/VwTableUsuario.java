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
@Table(name = "vw_table_usuario")
@Getter
@Setter
public class VwTableUsuario {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Nome\"")
    private String nome;

    @Column(name = "\"Email\"")
    private String email;

    @Column(name = "\"Username\"")
    private String username;

    @Column(name = "\"Cpf\"")
    private String cpf;

    @Column(name = "\"Situação\"")
    private String situacao;

    @Column(name = "\"Tipo\"")
    private String tipo;

    @Column(name = "\"Data de Emissão\"")
    private LocalDate dataEmissao;
}
