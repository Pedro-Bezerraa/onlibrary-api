package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableMulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTableMultaRepository extends JpaRepository<VwTableMulta, UUID> {
    List<VwTableMulta> findByFkIdBiblioteca(UUID fkIdBiblioteca);
    List<VwTableMulta> findByFkIdUsuario(UUID fkIdUsuario);

    // Método para a busca "todos"
    @Query(value = "SELECT * FROM vw_table_multa v WHERE v.fk_id_biblioteca = :bibliotecaId AND (" +
            "LOWER(v.\"Username\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Nome\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Bibliotecario\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Valor\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Data de emissão\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Data de vencimento\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Situação\") ILIKE LOWER(CONCAT('%', :value, '%'))" +
            ")", nativeQuery = true)
    List<VwTableMulta> searchByAllInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    // Métodos para filtros específicos
    @Query(value = "SELECT * FROM vw_table_multa v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Username\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableMulta> searchByUsernameInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_multa v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Situação\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableMulta> searchBySituacaoInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

}
