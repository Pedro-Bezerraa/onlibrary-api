package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByUsername(String identificacao);
    Optional<Usuario> findByEmail(String identificacao);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByCpf(String cpf);
}
