package com.onlibrary.onlibrary_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_perfil_usuario_biblioteca")
@Getter
@Setter
public class PerfilUsuarioBiblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal multaPadrao;

    @Column(name = "data_devolucao_padrao")
    private Integer dataDevolucaoPadrao;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @OneToMany(mappedBy = "perfilUsuarioBiblioteca")
    private List<UsuarioBiblioteca> usuarioBibliotecas = new ArrayList<>();
}
