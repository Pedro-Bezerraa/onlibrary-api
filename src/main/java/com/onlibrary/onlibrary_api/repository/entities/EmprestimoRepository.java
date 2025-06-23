package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Emprestimo;
import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, UUID> {
    @Query("SELECT CASE WHEN COUNT(emp) > 0 THEN true ELSE false END " +
            "FROM Emprestimo emp " +
            "JOIN emp.exemplares ee " +
            "WHERE ee.exemplar = :exemplar AND emp.situacao = :situacao")
    boolean existsByExemplarAndSituacao(
            @Param("exemplar") Exemplar exemplar,
            @Param("situacao") SituacaoEmprestimo situacao);
    
    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.biblioteca.id = :bibliotecaId AND e.situacao = 'PENDENTE'")
    boolean hasActiveEmprestimos(@Param("bibliotecaId") UUID bibliotecaId);

    long countByBibliotecaIdAndDeletadoIsFalse(UUID bibliotecaId);

    boolean existsByBibliotecaIdAndSituacaoAndDataDevolucaoBefore(UUID bibliotecaId, SituacaoEmprestimo situacao, LocalDate data);
}
