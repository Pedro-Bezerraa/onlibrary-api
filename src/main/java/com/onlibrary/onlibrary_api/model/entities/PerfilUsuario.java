package com.onlibrary.onlibrary_api.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_perfil_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    private String nome;
    private Integer multaPadrao;
    private Integer prazoDevolucaoPadrao;
    private Integer prazoMultaPadrao;

    @OneToMany(mappedBy = "perfilUsuario")
    private List<UsuarioBiblioteca> usuarios;
}
