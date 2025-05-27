package com.onlibrary.onlibrary_api.model.entities;

import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
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
    private String email;
    private String cpf;
    private String senha;

    @Enumerated(EnumType.STRING)
    private ContaSituacao situacao;
    private String username;

    @OneToMany(mappedBy = "usuario")
    private List<Notificacao> notificacoes;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioBiblioteca> usuarioBibliotecas;

    @OneToMany(mappedBy = "usuario")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "usuario")
    private List<Multa> multas;
}
