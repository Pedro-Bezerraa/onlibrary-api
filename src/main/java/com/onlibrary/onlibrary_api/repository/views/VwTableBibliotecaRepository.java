package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableBiblioteca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTableBibliotecaRepository extends JpaRepository<VwTableBiblioteca, UUID> {
    @Query(value = "SELECT * FROM vw_table_biblioteca WHERE LOWER(CAST(\"Nome\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBiblioteca> searchByNome(@Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_biblioteca WHERE LOWER(CAST(\"Telefone\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBiblioteca> searchByTelefone(@Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_biblioteca WHERE LOWER(CAST(\"Rua\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBiblioteca> searchByRua(@Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_biblioteca WHERE LOWER(CAST(\"Número\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBiblioteca> searchByNumero(@Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_biblioteca WHERE LOWER(CAST(\"Cep\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBiblioteca> searchByCep(@Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_biblioteca WHERE " +
            "LOWER(CAST(\"Nome\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(CAST(\"Telefone\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(CAST(\"Rua\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(CAST(\"Número\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(CAST(\"Cep\" AS TEXT)) ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableBiblioteca> searchByAll(@Param("value") String value);
}
