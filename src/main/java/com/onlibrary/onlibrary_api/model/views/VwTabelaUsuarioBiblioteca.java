package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_table_usuario_biblioteca")
@Getter
@Setter
public class VwTabelaUsuarioBiblioteca {

    private String username;

    private String nome;

    private String email;

    private String cpf;

    private String perfil;

    private String situacao;

    @Id
    private UUID id;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;

}
