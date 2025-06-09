package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.LivroEditora;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroEditoraRepository extends JpaRepository<LivroEditora, UUID> {
    void deleteByLivroId(UUID livroId);

}
