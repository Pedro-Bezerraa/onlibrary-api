package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableExemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTableExemplarRepository extends JpaRepository<VwTableExemplar, UUID> {
    List<VwTableExemplar> findByFkIdBiblioteca(UUID fkIdBiblioteca);

    @Query(value = "SELECT * FROM vw_table_exemplar v WHERE v.fk_id_biblioteca = :bibliotecaId AND (" +
            "LOWER(v.\"Titulo\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Número Tombo\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Estante\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Prateleira\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Setor\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Situação\") ILIKE LOWER(CONCAT('%', :value, '%'))" +
            ")", nativeQuery = true)
    List<VwTableExemplar> searchByAllInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_exemplar v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Titulo\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableExemplar> searchByTituloInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_exemplar v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Número Tombo\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableExemplar> searchByNumeroTomboInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_exemplar v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Situação\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableExemplar> searchBySituacaoInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);
}
