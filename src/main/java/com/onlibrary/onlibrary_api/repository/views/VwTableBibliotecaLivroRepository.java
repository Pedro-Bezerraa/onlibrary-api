package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaLivro;
import com.onlibrary.onlibrary_api.model.views.VwTableExemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTableBibliotecaLivroRepository extends JpaRepository<VwTableBibliotecaLivro, UUID> {
    List<VwTableBibliotecaLivro> findByFkIdBiblioteca(UUID fkIdBiblioteca);

    @Query(value = "SELECT * FROM vw_table_biblioteca_livro v WHERE v.fk_id_biblioteca = :bibliotecaId AND (" +
            "LOWER(v.\"Título\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"ISBN\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Autores\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Categorias\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Generos\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Editoras\") ILIKE LOWER(CONCAT('%', :value, '%'))" +
            ")", nativeQuery = true)
    List<VwTableBibliotecaLivro> searchByAllInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_biblioteca_livro v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Título\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBibliotecaLivro> searchByTituloInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_biblioteca_livro v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"ISBN\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBibliotecaLivro> searchByIsbnInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);
}
