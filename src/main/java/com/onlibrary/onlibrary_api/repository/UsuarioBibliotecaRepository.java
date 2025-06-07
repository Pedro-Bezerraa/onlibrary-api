package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.model.entities.UsuarioBiblioteca;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioBibliotecaRepository extends JpaRepository<UsuarioBiblioteca, UUID> {
    Optional<UsuarioBiblioteca> findByUsuarioIdAndBibliotecaId(UUID usuarioId, UUID bibliotecaId);
    List<UsuarioBiblioteca> findByUsuarioIdAndTipoUsuarioIn(UUID usuarioId, List<TipoUsuario> tipos);
    boolean existsByUsuarioId(UUID usuarioId);
}
