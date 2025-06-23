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
@Table(name = "vw_table_usuario_biblioteca")
@Getter
@Setter
public class VwTabelaUsuarioBiblioteca {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "username")
    @JsonProperty("Username")
    private String username;

    @Column(name = "nome")
    @JsonProperty("Nome")
    private String nome;

    @Column(name = "email")
    @JsonProperty("Email")
    private String email;

    @Column(name = "cpf")
    @JsonProperty("CPF")
    private String cpf;

    @Column(name = "perfil")
    @JsonProperty("Perfil")
    private String perfil;

    @Column(name = "situacao")
    @JsonProperty("Situação")
    private String situacao;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;
}
