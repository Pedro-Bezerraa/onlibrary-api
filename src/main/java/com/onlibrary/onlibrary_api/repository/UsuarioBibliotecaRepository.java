package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.UsuarioBiblioteca;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioBibliotecaRepository extends JpaRepository<UsuarioBiblioteca, UUID> {

    List<UsuarioBiblioteca> findByUsuarioIdAndTipoUsuario(UUID usuarioId, TipoUsuario tipos);
}
