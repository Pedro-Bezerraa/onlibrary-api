package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.Biblioteca;
import com.onlibrary.onlibrary_api.model.UsuarioBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioBibliotecaRepository extends JpaRepository<UsuarioBiblioteca, Long> {
    @Query("""
    SELECT ub.biblioteca FROM UsuarioBiblioteca ub
    WHERE ub.usuario.id = :usuarioId AND ub.tipoUsuario IN ('ADMIN_MASTER', 'FUNCIONARIO')
""")
    List<Biblioteca> findBibliotecasDoUsuarioAdministradorOuFuncionario(@Param("usuarioId") Long usuarioId);
}
