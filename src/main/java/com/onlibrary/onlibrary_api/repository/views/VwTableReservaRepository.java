package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTableReservaRepository extends JpaRepository<VwTableReserva, UUID> {
    List<VwTableReserva> findByFkIdBiblioteca(UUID fkIdBiblioteca);
    List<VwTableReserva> findByFkIdBibliotecaAndTipo(UUID fkIdBiblioteca, String tipo);

    List<VwTableReserva> findByFkIdUsuario(UUID fkIdUsuario);

    // Métodos para a rota de busca (Search)
    @Query(value = "SELECT * FROM vw_table_reserva v WHERE v.fk_id_biblioteca = :bibliotecaId AND (" +
            "LOWER(v.\"Username\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Livro\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Exemplares\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Data de Emissão\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Data de Retirada\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Situação\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.tipo) ILIKE LOWER(CONCAT('%', :value, '%'))" +
            ")", nativeQuery = true)
    List<VwTableReserva> searchByAllInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_reserva v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Username\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableReserva> searchByUsernameInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);
}
