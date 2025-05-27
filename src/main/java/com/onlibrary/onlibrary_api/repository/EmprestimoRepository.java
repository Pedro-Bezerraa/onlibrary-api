package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Emprestimo;
import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {

    @Query("SELECT CASE WHEN COUNT(emp) > 0 THEN true ELSE false END " +
            "FROM Emprestimo emp " +
            "JOIN emp.exemplares ee " +
            "WHERE ee.exemplar = :exemplar AND emp.situacao = :situacao")
    boolean existsByExemplarAndSituacao(
            @Param("exemplar") Exemplar exemplar,
            @Param("situacao") SituacaoEmprestimo situacao);

    @Query("SELECT e FROM Emprestimo e LEFT JOIN FETCH e.exemplares ex LEFT JOIN FETCH ex.exemplar WHERE e.id = :id")
    Optional<Emprestimo> findByIdWithExemplares(@Param("id") UUID id);
}
