package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, UUID> {
    boolean existsByNomeAndBibliotecaId(String nome, UUID bibliotecaId);
    List<PerfilUsuario> findByBiblioteca_Id(UUID bibliotecaId);
    boolean existsByNomeAndBibliotecaIdAndIdNot(String nome, UUID bibliotecaId, UUID id);
}
