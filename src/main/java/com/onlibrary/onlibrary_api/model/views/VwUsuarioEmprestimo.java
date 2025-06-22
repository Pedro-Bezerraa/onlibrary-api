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
@Table(name = "vw_usuario_emprestimo")
@Getter
@Setter
public class VwUsuarioEmprestimo {
    @Id
    @Column(name = "usuario_biblioteca_id")
    private UUID usuarioBibliotecaId;

    @Column(name = "username")
    private String username;
}
