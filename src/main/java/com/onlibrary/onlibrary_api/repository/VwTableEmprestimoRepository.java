package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.views.VwTableEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VwTableEmprestimoRepository extends JpaRepository<VwTableEmprestimo, UUID> {
    @Query(value = "SELECT * FROM \"vw_table_emprestimo\"", nativeQuery = true)
    List<VwTableEmprestimo> findAllEmprestimos();

    @Query(value = "SELECT * FROM \"vw_table_emprestimo\" WHERE \"Data de Devolução\" = TO_CHAR(current_date, 'DD/MM/YYYY')", nativeQuery = true)
    List<VwTableEmprestimo> findByDataDevolucaoHoje();
}
