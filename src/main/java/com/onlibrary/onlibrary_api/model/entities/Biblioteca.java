package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_biblioteca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Biblioteca {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;
    private String telefone;
    private String rua;
    private Integer numero;
    private String cep;

    @Column(name = "aplicacao_multa")
    private Boolean aplicacaoMulta;

    @Column(name = "reserva_online")
    private Boolean reservaOnline;

    @Column(name = "aplicacao_bloqueio")
    private Boolean aplicacaoBloqueio;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deletado = false;

    @OneToMany(mappedBy = "biblioteca")
    private List<UsuarioBiblioteca> usuarioBibliotecas;

    @OneToMany(mappedBy = "biblioteca")
    private List<PerfilUsuario> perfisUsuario;

    @OneToMany(mappedBy = "biblioteca")
    private List<Exemplar> exemplares;

    @OneToMany(mappedBy = "biblioteca")
    private List<Emprestimo> emprestimos;

    @OneToMany(mappedBy = "biblioteca")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "biblioteca")
    private List<Multa> multas;

    @OneToMany(mappedBy = "biblioteca")
    private List<Notificacao> notificacoes;

    @OneToMany(mappedBy = "biblioteca")
    private List<BibliotecaLivro> bibliotecaLivros;
}
