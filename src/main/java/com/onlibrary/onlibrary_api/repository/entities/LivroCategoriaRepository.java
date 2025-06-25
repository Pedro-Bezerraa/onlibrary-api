package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.LivroCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LivroCategoriaRepository extends JpaRepository<LivroCategoria, UUID> {
    void deleteByLivroId(UUID livroId);

    @Query("SELECT COUNT(lc) > 0 FROM LivroCategoria lc WHERE lc.categoria.id = :categoriaId AND lc.livro.deletado = false")
    boolean existsByCategoriaIdAndLivroDeletadoIsFalse(@Param("categoriaId") UUID categoriaId);
}
