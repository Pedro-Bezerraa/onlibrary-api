package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO;
import com.onlibrary.onlibrary_api.model.views.VwLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwLivroRepository extends JpaRepository<VwLivro, UUID> {

    public interface SuggestionProjection {
        String getSugestao();
        String getTipo();
    }

    @Query(value = """
        (SELECT l.titulo AS sugestao, 'titulo' as tipo
        FROM vw_livro AS l
        WHERE l.titulo ILIKE :value || '%')

        UNION

        (SELECT l.autores AS sugestao, 'autor' as tipo
        FROM vw_livro AS l
        WHERE l.autores ILIKE :value || '%')

        UNION

        (SELECT l.categorias AS sugestao, 'categoria' as tipo
        FROM vw_livro AS l
        WHERE l.categorias ILIKE :value || '%')

        UNION

        (SELECT l.generos AS sugestao, 'genero' as tipo
        FROM vw_livro AS l
        WHERE l.generos ILIKE :value || '%')

        UNION

        (SELECT l.editoras AS sugestao, 'editora' as tipo
        FROM vw_livro AS l
        WHERE l.editoras ILIKE :value || '%')

        LIMIT 5
    """, nativeQuery = true)
    List<SuggestionProjection> getSearchSuggestions(@Param("value") String value);

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

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v " +
            "WHERE LOWER(v.isbn) ILIKE LOWER(CONCAT('%', :value, '%'))")
    List<LivroHomePageSearchDTO> searchByIsbn(@Param("value") String value);

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v " +
            "WHERE LOWER(v.autores) ILIKE LOWER(CONCAT('%', :value, '%'))")
    List<LivroHomePageSearchDTO> searchByAutores(@Param("value") String value);

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v " +
            "WHERE LOWER(v.categorias) ILIKE LOWER(CONCAT('%', :value, '%'))")
    List<LivroHomePageSearchDTO> searchByCategorias(@Param("value") String value);

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v " +
            "WHERE LOWER(v.generos) ILIKE LOWER(CONCAT('%', :value, '%'))")
    List<LivroHomePageSearchDTO> searchByGeneros(@Param("value") String value);

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO(v.id, v.titulo, v.capa) FROM VwLivro v " +
            "WHERE LOWER(v.editoras) ILIKE LOWER(CONCAT('%', :value, '%'))")
    List<LivroHomePageSearchDTO> searchByEditoras(@Param("value") String value);
}
