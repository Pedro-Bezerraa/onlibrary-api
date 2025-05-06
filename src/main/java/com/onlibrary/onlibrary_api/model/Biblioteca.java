package com.onlibrary.onlibrary_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_biblioteca")
@Getter
@Setter
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefone;
    private Boolean aplicacaoMulta;
    private Boolean reservarOnline;

    @Embedded
    private Endereco endereco;

    @OneToMany(mappedBy = "biblioteca")
    private List<UsuarioBiblioteca> usuarioBibliotecas = new ArrayList<>();

    @OneToMany(mappedBy = "biblioteca")
    private List<PerfilUsuarioBiblioteca> perfis = new ArrayList<>();
}
