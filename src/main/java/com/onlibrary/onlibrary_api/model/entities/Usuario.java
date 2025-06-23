package com.onlibrary.onlibrary_api.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;
    private String sobrenome;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String cpf;
    private String senha;

    @Enumerated(EnumType.STRING)
    private ContaSituacao situacao;

    @Column(unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipo;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Notificacao> notificacoes;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioBiblioteca> usuarioBibliotecas;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "usuario")
    private List<Multa> multas;

    public Usuario(UUID id, String nome, String email, String senha, String username) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.username = username;
    }

    @Column(nullable = false)
    @Builder.Default
    private Boolean deletado = false;
}
