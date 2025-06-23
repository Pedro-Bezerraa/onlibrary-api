package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByUsername(String identificacao);
    Optional<Usuario> findByEmail(String identificacao);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);

    boolean existsByUsernameAndIdNot(String username, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.usuario.id = :usuarioId AND (r.situacao = 'PENDENTE' OR r.situacao = 'ATENDIDO_PARCIALMENTE' OR r.situacao = 'ATENDIDO_COMPLETAMENTE')")
    boolean hasActiveReservas(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.usuarioBiblioteca.usuario.id = :usuarioId AND e.situacao = 'PENDENTE'")
    boolean hasActiveEmprestimos(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT COUNT(m) > 0 FROM Multa m WHERE m.usuario.id = :usuarioId AND m.situacao = 'PENDENTE'")
    boolean hasActiveMultas(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT COUNT(ub) > 0 FROM UsuarioBiblioteca ub JOIN ub.biblioteca b WHERE ub.usuario.id = :usuarioId AND ub.tipoUsuario = 'ADMIN_MASTER' AND b.deletado = false")
    boolean isAdminMasterOfAnyBiblioteca(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT u FROM Usuario u WHERE u.deletado = false AND u.id NOT IN (SELECT ub.usuario.id FROM UsuarioBiblioteca ub WHERE ub.biblioteca.id = :bibliotecaId)")
    List<Usuario> findUsersNotInLibrary(@Param("bibliotecaId") UUID bibliotecaId);

    List<Usuario> findByDeletadoFalse();
}
