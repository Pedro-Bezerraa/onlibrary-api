package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.model.entities.UsuarioBiblioteca;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioBibliotecaRepository extends JpaRepository<UsuarioBiblioteca, UUID> {
    Optional<UsuarioBiblioteca> findByUsuarioIdAndBibliotecaId(UUID usuarioId, UUID bibliotecaId);
    List<UsuarioBiblioteca> findByUsuarioIdAndTipoUsuarioIn(UUID usuarioId, List<TipoUsuario> tipos);
    boolean existsByUsuarioIdAndBibliotecaId(UUID usuarioId, UUID bibliotecaId);

    @Query("SELECT COUNT(r) > 0 " +
            "FROM UsuarioBiblioteca ub " +
            "JOIN ub.usuario u " +
            "JOIN u.reservas r " +
            "WHERE ub.id = :usuarioBibliotecaId " +
            "AND r.biblioteca.id = ub.biblioteca.id " +
            "AND r.situacao IN ('PENDENTE', 'ATENDIDO_PARCIALMENTE', 'ATENDIDO_COMPLETAMENTE')")
    boolean hasActiveReservasByUsuarioBibliotecaId(@Param("usuarioBibliotecaId") UUID usuarioBibliotecaId);

    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.usuarioBiblioteca.id = :usuarioBibliotecaId AND e.situacao = 'PENDENTE'")
    boolean hasPendingEmprestimosByUsuarioBibliotecaId(@Param("usuarioBibliotecaId") UUID usuarioBibliotecaId);
}
