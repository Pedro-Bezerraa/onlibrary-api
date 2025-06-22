package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BibliotecaRepository extends JpaRepository<Biblioteca, UUID> {
    Optional<Biblioteca> findByNome(String nome);

}
