package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, UUID> {
    boolean existsByNomeAndBibliotecaId(String nome, UUID bibliotecaId);
    boolean existsByNomeAndBibliotecaIdAndIdNot(String nome, UUID bibliotecaId, UUID id);
    @Query("SELECT COUNT(ub) > 0 FROM UsuarioBiblioteca ub WHERE ub.perfilUsuario.id = :perfilUsuarioId AND ub.deletado = FALSE")
    boolean existsActiveUsuarioBibliotecaByPerfilUsuarioId(@Param("perfilUsuarioId") UUID perfilUsuarioId);
    long countByBibliotecaIdAndDeletadoFalse(UUID bibliotecaId);
    List<PerfilUsuario> findByBibliotecaIdAndDeletadoFalse(UUID bibliotecaId);
}
