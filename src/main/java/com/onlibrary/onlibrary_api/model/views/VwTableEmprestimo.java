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
@Table(name = "vw_table_emprestimo")
@Getter
@Setter
public class VwTableEmprestimo {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "\"Username\"")
    private String username;

    @Column(name = "\"Livros\"")
    private String livros;

    @Column(name = "\"Exemplares\"")
    private String exemplares;

    @Column(name = "\"Bibliotecario\"")
    private String bibliotecario;

    @Column(name = "\"Data de Emissão\"")
    private String dataEmissao;

    @Column(name = "\"Data de Devolução\"")
    private String dataDevolucao;

    @Column(name = "\"Situação\"")
    private String situacao;

    @Column(name = "fk_id_biblioteca")
    private UUID fkIdBiblioteca;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;
}
