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

    @Column(name = "multa_padrao")
    private Integer multaPadrao;

    @Column(name = "prazo_devolucao_padrao")
    private Integer prazoDevolucaoPadrao;

    @Column(name = "prazo_multa_padrao")
    private Integer prazoMultaPadrao;

    @OneToMany(mappedBy = "perfilUsuario")
    private List<UsuarioBiblioteca> usuarios;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deletado = false;
}
