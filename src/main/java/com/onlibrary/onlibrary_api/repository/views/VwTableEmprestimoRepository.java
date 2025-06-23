package com.onlibrary.onlibrary_api.repository.views;

import com.onlibrary.onlibrary_api.model.views.VwTableEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface VwTableEmprestimoRepository extends JpaRepository<VwTableEmprestimo, UUID> {
    @Query(value = "SELECT * FROM \"vw_table_emprestimo\"", nativeQuery = true)
    List<VwTableEmprestimo> findAllEmprestimos();

    @Query(value = "SELECT * FROM \"vw_table_emprestimo\" WHERE \"Data de Devolução\" = TO_CHAR(current_date, 'DD/MM/YYYY')", nativeQuery = true)
    List<VwTableEmprestimo> findByDataDevolucaoHoje();

    List<VwTableEmprestimo> findByFkIdBiblioteca(UUID fkIdBiblioteca);

    List<VwTableEmprestimo> findByFkIdUsuario(UUID fkIdUsuario);

    // Métodos para a rota de busca (Search)
    @Query(value = "SELECT * FROM vw_table_emprestimo v WHERE v.fk_id_biblioteca = :bibliotecaId AND (" +
            "LOWER(v.\"Username\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Livros\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Exemplares\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Bibliotecario\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Data de Emissão\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Data de Devolução\") ILIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(v.\"Situação\") ILIKE LOWER(CONCAT('%', :value, '%'))" +
            ")", nativeQuery = true)
    List<VwTableEmprestimo> searchByAllInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);

    @Query(value = "SELECT * FROM vw_table_emprestimo v WHERE v.fk_id_biblioteca = :bibliotecaId AND " +
            "LOWER(v.\"Username\") ILIKE LOWER(CONCAT('%', :value, '%'))", nativeQuery = true)
    List<VwTableEmprestimo> searchByUsernameInBiblioteca(@Param("bibliotecaId") UUID bibliotecaId, @Param("value") String value);
}
