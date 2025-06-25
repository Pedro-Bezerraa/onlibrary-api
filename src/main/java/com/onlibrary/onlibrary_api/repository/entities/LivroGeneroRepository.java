package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.LivroGenero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LivroGeneroRepository extends JpaRepository<LivroGenero, UUID> {
    void deleteByLivroId(UUID livroId);

    @Query("SELECT COUNT(lg) > 0 FROM LivroGenero lg WHERE lg.genero.id = :generoId AND lg.livro.deletado = false")
    boolean existsByGeneroIdAndLivroDeletadoIsFalse(@Param("generoId") UUID generoId);
}
