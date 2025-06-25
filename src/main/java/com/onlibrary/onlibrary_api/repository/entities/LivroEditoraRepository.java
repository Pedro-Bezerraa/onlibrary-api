package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.LivroEditora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LivroEditoraRepository extends JpaRepository<LivroEditora, UUID> {
    void deleteByLivroId(UUID livroId);

    @Query("SELECT COUNT(le) > 0 FROM LivroEditora le WHERE le.editora.id = :editoraId AND le.livro.deletado = false")
    boolean existsByEditoraIdAndLivroDeletadoIsFalse(@Param("editoraId") UUID editoraId);
}
