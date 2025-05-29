package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, UUID> {
    boolean existsByNome(String nome);
}
