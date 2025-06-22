package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.LivroCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroCategoriaRepository extends JpaRepository<LivroCategoria, UUID> {
    void deleteByLivroId(UUID livroId);

}
