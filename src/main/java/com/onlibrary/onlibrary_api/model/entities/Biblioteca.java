package com.onlibrary.onlibrary_api.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Column(name = "data_emissao")
    @Builder.Default
    private LocalDate dataEmissao = LocalDate.now();

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<UsuarioBiblioteca> usuarioBibliotecas;

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<PerfilUsuario> perfisUsuario;

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<Exemplar> exemplares;

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<Emprestimo> emprestimos;

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<Multa> multas;

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<Notificacao> notificacoes;

    @OneToMany(mappedBy = "biblioteca")
    @JsonIgnore
    private List<BibliotecaLivro> bibliotecaLivros;
}
