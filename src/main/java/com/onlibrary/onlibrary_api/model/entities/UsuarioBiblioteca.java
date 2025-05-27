package com.onlibrary.onlibrary_api.model.entities;

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
@Table(name = "tb_usuario_biblioteca")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioBiblioteca {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_id_biblioteca")
    private Biblioteca biblioteca;

    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "fk_id_perfil_usuario")
    private PerfilUsuario perfilUsuario;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    private String numeroMatricula;
    private String cpf;

    @Enumerated(EnumType.STRING)
    private ContaSituacao situacao;

    @OneToMany(mappedBy = "usuarioBiblioteca")
    private List<Emprestimo> emprestimos;
}
