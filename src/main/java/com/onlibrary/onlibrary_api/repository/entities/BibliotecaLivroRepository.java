package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.BibliotecaLivro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BibliotecaLivroRepository extends JpaRepository<BibliotecaLivro, UUID> {
    boolean existsByBibliotecaIdAndLivroId(UUID bibliotecaId, UUID livroId);
    long countByBibliotecaId(UUID bibliotecaID);
}
