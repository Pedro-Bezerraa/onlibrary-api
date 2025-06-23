package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO;
import com.onlibrary.onlibrary_api.model.views.VwLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwLivroRepository extends JpaRepository<VwLivro, UUID> {
    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v " +
            "WHERE LOWER(v.titulo) ILIKE LOWER(CONCAT('%', :value, '%'))")
    List<LivroHomePageSearchDTO> searchByTitulo(@Param("value") String value);

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v " +
            "WHERE LOWER(v.titulo) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.descricao) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.autores) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.categorias) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.generos) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.editoras) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.isbn) ILIKE LOWER(CONCAT('%', :value, '%'))")
    List<LivroHomePageSearchDTO> searchByAll(@Param("value") String value);

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v")
    List<LivroHomePageSearchDTO> findAllAsHomePageSearchDTO();
}
