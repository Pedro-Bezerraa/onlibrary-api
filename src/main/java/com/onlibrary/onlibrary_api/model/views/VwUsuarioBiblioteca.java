package com.onlibrary.onlibrary_api.model.views;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Table(name = "vw_usuario_biblioteca")
@Getter
@Setter
public class VwUsuarioBiblioteca {
    @Id
    @Column(name = "usuario_biblioteca_id")
    private UUID usuarioBibliotecaId;

    @Column(name = "username")
    @JsonProperty("Username")
    private String username;

    @Column(name = "fk_id_usuario")
    private UUID fkIdUsuario;
}
