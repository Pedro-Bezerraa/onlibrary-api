package com.onlibrary.onlibrary_api.model.views;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "vw_usuario_biblioteca")
@Getter
@Setter
public class VwUsuarioBiblioteca {
    @Column(name = "username")
    private String username;

    @Id
    @Column(name = "usuario_biblioteca_id")
    private UUID usuarioBibliotecaId;
}
