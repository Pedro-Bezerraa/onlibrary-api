package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.BibliotecaLivro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BibliotecaLivroRepository extends JpaRepository<BibliotecaLivro, UUID> {
    long countByBibliotecaId(UUID bibliotecaID);
}
